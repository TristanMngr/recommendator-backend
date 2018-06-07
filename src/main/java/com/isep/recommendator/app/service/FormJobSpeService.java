package com.isep.recommendator.app.service;

import com.isep.recommendator.app.custom_object.Form1Response;
import com.isep.recommendator.app.custom_object.SpecialityWithMatchingJobs;
import com.isep.recommendator.app.model.Job;
import com.isep.recommendator.app.model.Speciality;
import com.isep.recommendator.app.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FormJobSpeService extends Utils<Form1Response> {
    private static final String MATCHING_JOB    = "matching_jobs";
    private static final String NO_MATCHING_JOB = "no_matching_jobs";

    protected HashMap<Speciality, HashMap<String, List<Job>>> speWithMatchOrNoMatchJob;

    @Autowired
    private JobRepository jobRepository;


    public List getForm1Response(List<Long> job_ids) {
        this.speWithMatchOrNoMatchJob = new HashMap<>();

        List<Object[]> speWithMatchingJob    = jobRepository.findSpecialityWithMatchingJobsByJobIds(job_ids);
        List<Object[]> speWithoutMatchingJob = jobRepository.findSpecialityWithoutMatchingJobsByJobIds(job_ids);

        buildMapSpecialityJobs(speWithMatchingJob, true);
        buildMapSpecialityJobs(speWithoutMatchingJob, false);

        return buildResponse();
    }


    protected void buildMapSpecialityJobs(List<Object[]> speWithJobs, boolean isQueryMatching) {
        String matchOrNotMatch = isQueryMatching ? MATCHING_JOB : NO_MATCHING_JOB;

        for (Object[] object : speWithJobs) {
            Speciality speciality = (Speciality) object[0];
            Job        job        = (Job) object[1];

            if (!speWithMatchOrNoMatchJob.keySet().contains(speciality)) {
                List<Job> jobs = new ArrayList<>();
                jobs.add(job);

                HashMap<String, List<Job>> matchOrNotMatchJob = new HashMap<>();
                matchOrNotMatchJob.put(matchOrNotMatch, jobs);

                speWithMatchOrNoMatchJob.put(speciality, matchOrNotMatchJob);
            } else {
                if (!speWithMatchOrNoMatchJob.get(speciality).containsKey(matchOrNotMatch)) {
                    speWithMatchOrNoMatchJob.get(speciality).put(matchOrNotMatch, new ArrayList<>());
                }

                speWithMatchOrNoMatchJob.get(speciality).get(matchOrNotMatch).add(job);
            }
        }
    }


    protected List<Form1Response> buildResponse() {
        List<Form1Response> form1Responses = new ArrayList<>();
        speWithMatchOrNoMatchJob.forEach((key, value) -> {
            SpecialityWithMatchingJobs specialityWithMatchingJobs = new SpecialityWithMatchingJobs(key, value.get(MATCHING_JOB), value.get(NO_MATCHING_JOB));

            int     matchingPourcentage = this.calculateMatchingForm1(value.get(MATCHING_JOB), value.get(NO_MATCHING_JOB));
            boolean isMatching          = matchingPourcentage > 0;

            Form1Response form1Response = new Form1Response(isMatching, matchingPourcentage, specialityWithMatchingJobs);
            form1Responses.add(form1Response);
        });

        sortWithAttributes(form1Responses);

        return form1Responses;
    }

    private int calculateMatchingForm1(List<Job> listMatching, List<Job> listNoMatching) {
        int valueMatching   = (listMatching != null) ? listMatching.size() : 0;
        int valueNoMatching = (listNoMatching != null) ? listNoMatching.size() : 0;
        return (valueMatching * 100) / (valueNoMatching + valueMatching);
    }
}
