package com.dev.myapp.service;

import com.dev.myapp.domain.Utilisateur;
import com.dev.myapp.repository.UtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Utilisateur.
 */
@Service
@Transactional
public class UtilisateurService {

    private final Logger log = LoggerFactory.getLogger(UtilisateurService.class);
    
    @Inject
    private UtilisateurRepository utilisateurRepository;
    
    /**
     * Save a utilisateur.
     * 
     * @param utilisateur the entity to save
     * @return the persisted entity
     */
    public Utilisateur save(Utilisateur utilisateur) {
        log.debug("Request to save Utilisateur : {}", utilisateur);
        Utilisateur result = utilisateurRepository.save(utilisateur);
        return result;
    }

    /**
     *  Get all the utilisateurs.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Utilisateur> findAll(Pageable pageable) {
        log.debug("Request to get all Utilisateurs");
        Page<Utilisateur> result = utilisateurRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one utilisateur by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Utilisateur findOne(Long id) {
        log.debug("Request to get Utilisateur : {}", id);
        Utilisateur utilisateur = utilisateurRepository.findOne(id);
        return utilisateur;
    }

    /**
     *  Delete the  utilisateur by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Utilisateur : {}", id);
        utilisateurRepository.delete(id);
    }
}
