package com.dev.myapp.web.rest;

import com.dev.myapp.MyApp;
import com.dev.myapp.domain.Utilisateur;
import com.dev.myapp.repository.UtilisateurRepository;
import com.dev.myapp.service.UtilisateurService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the UtilisateurResource REST controller.
 *
 * @see UtilisateurResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyApp.class)
@WebAppConfiguration
@IntegrationTest
public class UtilisateurResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Long DEFAULT_UTILISATEUR_ID = 1L;
    private static final Long UPDATED_UTILISATEUR_ID = 2L;
    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";
    private static final String DEFAULT_PRENOM = "AAAAA";
    private static final String UPDATED_PRENOM = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_PHONE_NUMBER = "AAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBB";

    private static final ZonedDateTime DEFAULT_DATE_DE_NAISASNCE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_DE_NAISASNCE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_DE_NAISASNCE_STR = dateTimeFormatter.format(DEFAULT_DATE_DE_NAISASNCE);

    @Inject
    private UtilisateurRepository utilisateurRepository;

    @Inject
    private UtilisateurService utilisateurService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUtilisateurMockMvc;

    private Utilisateur utilisateur;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UtilisateurResource utilisateurResource = new UtilisateurResource();
        ReflectionTestUtils.setField(utilisateurResource, "utilisateurService", utilisateurService);
        this.restUtilisateurMockMvc = MockMvcBuilders.standaloneSetup(utilisateurResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        utilisateur = new Utilisateur();
        utilisateur.setUtilisateurId(DEFAULT_UTILISATEUR_ID);
        utilisateur.setNom(DEFAULT_NOM);
        utilisateur.setPrenom(DEFAULT_PRENOM);
        utilisateur.setEmail(DEFAULT_EMAIL);
        utilisateur.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        utilisateur.setDateDeNaisasnce(DEFAULT_DATE_DE_NAISASNCE);
    }

    @Test
    @Transactional
    public void createUtilisateur() throws Exception {
        int databaseSizeBeforeCreate = utilisateurRepository.findAll().size();

        // Create the Utilisateur

        restUtilisateurMockMvc.perform(post("/api/utilisateurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(utilisateur)))
                .andExpect(status().isCreated());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        assertThat(utilisateurs).hasSize(databaseSizeBeforeCreate + 1);
        Utilisateur testUtilisateur = utilisateurs.get(utilisateurs.size() - 1);
        assertThat(testUtilisateur.getUtilisateurId()).isEqualTo(DEFAULT_UTILISATEUR_ID);
        assertThat(testUtilisateur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testUtilisateur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testUtilisateur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUtilisateur.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testUtilisateur.getDateDeNaisasnce()).isEqualTo(DEFAULT_DATE_DE_NAISASNCE);
    }

    @Test
    @Transactional
    public void getAllUtilisateurs() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurs
        restUtilisateurMockMvc.perform(get("/api/utilisateurs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(utilisateur.getId().intValue())))
                .andExpect(jsonPath("$.[*].utilisateurId").value(hasItem(DEFAULT_UTILISATEUR_ID.intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].dateDeNaisasnce").value(hasItem(DEFAULT_DATE_DE_NAISASNCE_STR)));
    }

    @Test
    @Transactional
    public void getUtilisateur() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get the utilisateur
        restUtilisateurMockMvc.perform(get("/api/utilisateurs/{id}", utilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(utilisateur.getId().intValue()))
            .andExpect(jsonPath("$.utilisateurId").value(DEFAULT_UTILISATEUR_ID.intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.dateDeNaisasnce").value(DEFAULT_DATE_DE_NAISASNCE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingUtilisateur() throws Exception {
        // Get the utilisateur
        restUtilisateurMockMvc.perform(get("/api/utilisateurs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUtilisateur() throws Exception {
        // Initialize the database
        utilisateurService.save(utilisateur);

        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();

        // Update the utilisateur
        Utilisateur updatedUtilisateur = new Utilisateur();
        updatedUtilisateur.setId(utilisateur.getId());
        updatedUtilisateur.setUtilisateurId(UPDATED_UTILISATEUR_ID);
        updatedUtilisateur.setNom(UPDATED_NOM);
        updatedUtilisateur.setPrenom(UPDATED_PRENOM);
        updatedUtilisateur.setEmail(UPDATED_EMAIL);
        updatedUtilisateur.setPhoneNumber(UPDATED_PHONE_NUMBER);
        updatedUtilisateur.setDateDeNaisasnce(UPDATED_DATE_DE_NAISASNCE);

        restUtilisateurMockMvc.perform(put("/api/utilisateurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUtilisateur)))
                .andExpect(status().isOk());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        assertThat(utilisateurs).hasSize(databaseSizeBeforeUpdate);
        Utilisateur testUtilisateur = utilisateurs.get(utilisateurs.size() - 1);
        assertThat(testUtilisateur.getUtilisateurId()).isEqualTo(UPDATED_UTILISATEUR_ID);
        assertThat(testUtilisateur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testUtilisateur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testUtilisateur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUtilisateur.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testUtilisateur.getDateDeNaisasnce()).isEqualTo(UPDATED_DATE_DE_NAISASNCE);
    }

    @Test
    @Transactional
    public void deleteUtilisateur() throws Exception {
        // Initialize the database
        utilisateurService.save(utilisateur);

        int databaseSizeBeforeDelete = utilisateurRepository.findAll().size();

        // Get the utilisateur
        restUtilisateurMockMvc.perform(delete("/api/utilisateurs/{id}", utilisateur.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        assertThat(utilisateurs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
