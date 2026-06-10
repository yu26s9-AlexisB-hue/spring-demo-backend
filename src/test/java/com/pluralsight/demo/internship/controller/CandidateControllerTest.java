package com.pluralsight.demo.internship.controller;

import com.pluralsight.demo.internship.model.Candidate;
import com.pluralsight.demo.internship.service.CandidateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CandidateController.class)
class CandidateControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CandidateService candidateService;

    @Test
    void getAllCandidates_shouldReturnListOfCandidates() throws Exception{
        Candidate candidate = new Candidate("Jason Voorhees", "CrystalLakeBlues@yahoo.com","Slicing Software");
        candidate.setId(1L);

        Candidate candidate2 = new Candidate("Ellen Ripley", "ripley.nostromo@gmail.com", "Weyland-Yutani Systems");
        candidate2.setId(2L);

        Candidate candidate3 = new Candidate("Sarah Connor", "sconnor1984@yahoo.com", "Skynet Defense Solutions");
        candidate3.setId(3L);

        List<Candidate> candidates = Arrays.asList(candidate,candidate2,candidate3);

        when(candidateService.getAllCandidates()).thenReturn(candidates);

        mockMvc.perform(get("/api/candidates").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Jason Voorhees"))
                .andExpect(jsonPath("$[0].email").value("CrystalLakeBlues@yahoo.com"))
                .andExpect(jsonPath("$[1].name").value("Ellen Ripley"))
                .andExpect(jsonPath("$[1].fieldOfStudy").value("Weyland-Yutani Systems"))
                .andExpect(jsonPath("$[2].email").value("sconnor1984@yahoo.com"))
                .andExpect(jsonPath("$[2].fieldOfStudy").value("Skynet Defense Solutions"))
                .andExpect(jsonPath("$.length()").value(3));


    }

    @Test
    void createCandidate_shouldReturnCreatedCandidate() throws Exception{

        Candidate savedCandidate = new Candidate("Freddy Krueger", "dreamstalker@gmail.com", "Neuroscience");
        savedCandidate.setId(4L);

        when(candidateService.createCandidate(any(Candidate.class))).thenReturn(savedCandidate);

        mockMvc.perform(post("/api/candidates").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "name": "Freddy Krueger",
                        "email": "dreamstalker@gmail.com",
                        "fieldOfStudy": "Neuroscience"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.name").value("Freddy Krueger"))
                .andExpect(jsonPath("$.email").value("dreamstalker@gmail.com"))
                .andExpect(jsonPath("$.fieldOfStudy").value("Neuroscience"));
    }

    @Test
    void deleteCandidate_shouldReturnNoContent() throws Exception{
        Long id = 5L;
        doNothing().when(candidateService).deleteCandidate(id);

        mockMvc.perform(delete("/api/candidates/{id}", id))
                .andExpect(status().isNoContent());

        verify(candidateService, times(1)).deleteCandidate(id);
    }

    @Test
    void getCandidateById_shouldReturnCandidates() throws Exception{
        Candidate idCandidate = new Candidate("Michael Myers", "Haddonfield1031@gmail.com","Slicing Software");
        idCandidate.setId(6L);

        when(candidateService.getCandidateById(anyLong())).thenReturn(idCandidate);

        mockMvc.perform(get("/api/candidates/{id}",6))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Michael Myers"))
                .andExpect(jsonPath("$.email").value("Haddonfield1031@gmail.com"))
                .andExpect(jsonPath("$.fieldOfStudy").value("Slicing Software"));
    }

}