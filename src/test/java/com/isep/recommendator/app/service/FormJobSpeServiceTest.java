package com.isep.recommendator.app.service;

import com.isep.recommendator.app.Application;
import com.isep.recommendator.app.custom_object.Form1Response;
import com.isep.recommendator.app.model.Job;
import com.isep.recommendator.app.model.Speciality;
import com.isep.recommendator.app.repository.*;
import com.isep.recommendator.security.config.WebSecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, WebSecurityConfig.class})
public class FormJobSpeServiceTest {

    @Autowired
    JobRepository        jobRepository;
    @Autowired
    SpecialityRepository specialityRepository;

    @Autowired
    JobService        jobService;
    @Autowired
    SpecialityService specialityService;

    @Autowired
    FormJobSpeService formJobSpeService;

    private Job job_one;
    private Job job_two;
    private Job job_three;

    private Speciality spe_one;
    private Speciality spe_two;
    private Speciality spe_three;
    private Speciality spe_four;

    @Before
    public void before() {
        this.initDB();
        this.makeLinks();
    }

    private void initDB() {
        spe_one = specialityRepository.save(new Speciality("speciality 1", "ceci est la spe 1"));
        spe_two = specialityRepository.save(new Speciality("speciality 2", "ceci est la spe 2"));
        spe_three = specialityRepository.save(new Speciality("speciality 3", "ceci est la spe 3"));
        spe_four = specialityRepository.save(new Speciality("speciality 4", "ceci est la spe 4"));

        job_one = jobRepository.save(new Job("job 1"));
        job_two = jobRepository.save(new Job("job 2"));
        job_three = jobRepository.save(new Job("job 3"));
    }

    private void makeLinks() {
        spe_one = specialityService.addJob(spe_one.getId(), job_one.getId());
        spe_one = specialityService.addJob(spe_one.getId(), job_two.getId());

        spe_two = specialityService.addJob(spe_two.getId(), job_three.getId());
        spe_three = specialityService.addJob(spe_three.getId(), job_one.getId());
    }

    @Test
    public void repositoryRequest() {
        Speciality speciality;
        Job        job;

        ArrayList<Long> job_ids = new ArrayList<>();
        job_ids.add(job_one.getId());
        job_ids.add(job_two.getId());

        List<Object[]> specialityWithMatchingJobs    = jobRepository.findSpecialityWithMatchingJobsByJobIds(job_ids);
        List<Object[]> specialityWithoutMatchingJobs = jobRepository.findSpecialityWithoutMatchingJobsByJobIds(job_ids);

        // specialityWithMatchingJobs

        assertTrue("it should contains 3 elements", specialityWithMatchingJobs.size() == 3);

        speciality = (Speciality) specialityWithMatchingJobs.get(0)[0];
        job = (Job) specialityWithMatchingJobs.get(0)[1];

        assertTrue("it should be equal to spe_one", speciality.getId() == spe_one.getId());
        assertTrue("it should be equal to job_one or job_two", job_ids.contains(job.getId()));

        speciality = (Speciality) specialityWithMatchingJobs.get(1)[0];
        job = (Job) specialityWithMatchingJobs.get(1)[1];
        assertTrue("it should be equal to spe_one", speciality.getId() == spe_one.getId());
        assertTrue("it should be equal to job_one or job_two", job_ids.contains(job.getId()));

        speciality = (Speciality) specialityWithMatchingJobs.get(2)[0];
        job = (Job) specialityWithMatchingJobs.get(2)[1];
        assertTrue("it should be equal to spe_three", speciality.getId() == spe_three.getId());
        assertTrue("it should be equal to job_one", job.getId() == job_one.getId());


        // specialityWithoutMatchingJobs

        assertTrue("it should contains 1 elements", specialityWithoutMatchingJobs.size() == 1);

        speciality = (Speciality) specialityWithoutMatchingJobs.get(0)[0];
        job = (Job) specialityWithoutMatchingJobs.get(0)[1];

        assertTrue("it should be equal to spe_two", speciality.getId() == spe_two.getId());
        assertTrue("it should be equal to job_three", job.getId() == job_three.getId());
    }

    @Test
    public void buildMapSpecialityJobs() {
        ArrayList<Long> job_ids = new ArrayList<>();
        job_ids.add(job_one.getId());
        job_ids.add(job_two.getId());

        // with matching jobs
        formJobSpeService.speWithMatchOrNoMatchJob = new HashMap<>();
        List<Object[]> speWithMatchingJob = jobRepository.findSpecialityWithMatchingJobsByJobIds(job_ids);

        formJobSpeService.buildMapSpecialityJobs(speWithMatchingJob, true);

        List<Long> specialitiesIdsMatchingJobs = new ArrayList<>();
        formJobSpeService.speWithMatchOrNoMatchJob.keySet().stream().map(Speciality::getId).sorted()
                .forEach(id -> specialitiesIdsMatchingJobs.add(id));

        System.out.println("here");
        System.out.println(specialitiesIdsMatchingJobs);

        assertTrue("it should contains 2 elements", specialitiesIdsMatchingJobs.size() == 2);
        assertTrue("it should contains spe_one and spe_three", specialitiesIdsMatchingJobs.containsAll(Arrays.asList(new Long[]{1L, 3L})));

        List<Long> matchingJobsIds = new ArrayList<>();
        formJobSpeService.speWithMatchOrNoMatchJob.keySet().stream()
                .forEach(speciality -> formJobSpeService.speWithMatchOrNoMatchJob.get(speciality).get("matching_jobs")
                        .forEach(matching -> matchingJobsIds.add(matching.getId())));

        Collections.sort(matchingJobsIds);

        assertTrue("it should contains two time job_one and one time job_two",
                matchingJobsIds.containsAll(Arrays.asList(new Long[]{1L, 1L, 2L})));


        // without matching jobs

        formJobSpeService.speWithMatchOrNoMatchJob = new HashMap<>();
        List<Object[]> speWithoutMatchingJobs = jobRepository.findSpecialityWithoutMatchingJobsByJobIds(job_ids);

        formJobSpeService.buildMapSpecialityJobs(speWithoutMatchingJobs, false);

        List<Long> specialitiesIdsNoMatchingJobs = new ArrayList<>();

        formJobSpeService.speWithMatchOrNoMatchJob.keySet().stream().map(Speciality::getId).sorted()
                .forEach(id -> specialitiesIdsNoMatchingJobs.add(id));

        assertTrue("it should contains 1 elements", specialitiesIdsNoMatchingJobs.size() == 1);
        assertTrue("it should contains spe_two", specialitiesIdsNoMatchingJobs.containsAll(Arrays.asList(new Long[]{2L})));

        List<Long> matchingJobsIdsNoMatchingJobs = new ArrayList<>();
        formJobSpeService.speWithMatchOrNoMatchJob.keySet().stream()
                .forEach(speciality -> formJobSpeService.speWithMatchOrNoMatchJob.get(speciality).get("no_matching_jobs")
                        .forEach(matching -> matchingJobsIdsNoMatchingJobs.add(matching.getId())));

        Collections.sort(matchingJobsIdsNoMatchingJobs);

        assertTrue("it should contains job_three", matchingJobsIdsNoMatchingJobs.containsAll(Arrays.asList(new Long[]{3L})));



    }

    @Test
    public void buildResponse() {
        ArrayList<Long> job_ids = new ArrayList<>();
        job_ids.add(job_one.getId());
        job_ids.add(job_two.getId());

        formJobSpeService.speWithMatchOrNoMatchJob = new HashMap<>();
        List<Object[]> speWithoutMatchingJobs = jobRepository.findSpecialityWithoutMatchingJobsByJobIds(job_ids);
        List<Object[]> speWithMatchingJob = jobRepository.findSpecialityWithMatchingJobsByJobIds(job_ids);

        formJobSpeService.buildMapSpecialityJobs(speWithMatchingJob, true);
        formJobSpeService.buildMapSpecialityJobs(speWithoutMatchingJobs, false);

        List<Form1Response> form1Responses = formJobSpeService.buildResponse();

        assertTrue("it should contains 3 elements", form1Responses.size() == 3);

        List<Long> speThreeMatchingJobs = new ArrayList<>();
        form1Responses.get(1).getSpeciality().getMatching_jobs().stream().map(Job::getId).sorted().forEach(jobId -> speThreeMatchingJobs.add(jobId));
        assertTrue("second element should contain speciality 3 or 1", Arrays.asList(new Long[]{1L,3L}).contains(form1Responses.get(1).getSpeciality().getId()));
        assertTrue("speciality 3 no matching jobs null", form1Responses.get(1).getSpeciality().getNo_matching_jobs() == null);
        assertTrue("speciality 3 with a match 100", form1Responses.get(1).getMatching() == 100);


        List<Long> speOneMatchingJobs = new ArrayList<>();
        form1Responses.get(0).getSpeciality().getMatching_jobs().stream().map(Job::getId).sorted().forEach(jobId -> speOneMatchingJobs.add(jobId));
        assertTrue("first element should contain speciality 1 or 3", Arrays.asList(new Long[]{1L,3L}).contains(form1Responses.get(0).getSpeciality().getId()));
        assertTrue("speciality 1 no matching null", form1Responses.get(0).getSpeciality().getNo_matching_jobs() == null);
        assertTrue("speciality 1 with a match 100", form1Responses.get(0).getMatching() == 100);


        List<Long> speTwoNoMatchingJobs = new ArrayList<>();
        form1Responses.get(2).getSpeciality().getNo_matching_jobs().stream().map(Job::getId).sorted().forEach(jobId -> speTwoNoMatchingJobs.add(jobId));
        assertTrue("third element should contain speciality 2", form1Responses.get(2).getSpeciality().getId() == 2);
        assertTrue("speciality 2 matching null", form1Responses.get(2).getSpeciality().getMatching_jobs() == null);
        assertTrue("speciality 2 no matching job 3", speTwoNoMatchingJobs.containsAll(Arrays.asList(new Long[]{3L})));
        assertTrue("speciality 2 with a match 0", form1Responses.get(2).getMatching() == 0);
    }
}