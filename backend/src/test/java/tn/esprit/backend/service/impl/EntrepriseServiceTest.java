package tn.esprit.backend.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.backend.entity.Entreprise;
import tn.esprit.backend.repository.EntrepriseRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntrepriseServiceTest {

    @Mock
    private EntrepriseRepository entrepriseRepository;

    @InjectMocks
    private EntrepriseServiceImpl entrepriseService;

    private Entreprise entreprise1;
    private Entreprise entreprise2;

    @BeforeEach
    void setUp() {
        entreprise1 = Entreprise.builder().id(1L).nom("ESPRIT").adresse("Tunis").build();
        entreprise2 = Entreprise.builder().id(2L).nom("Google").adresse("USA").build();
    }

    @Test
    @DisplayName("addEntreprise - doit sauvegarder et retourner l entreprise")
    void testAddEntreprise() {
        when(entrepriseRepository.save(any(Entreprise.class))).thenReturn(entreprise1);
        Entreprise result = entrepriseService.addEntreprise(entreprise1);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ESPRIT", result.getNom());
        assertEquals("Tunis", result.getAdresse());
        verify(entrepriseRepository, times(1)).save(entreprise1);
    }

    @Test
    @DisplayName("updateEntreprise - doit mettre a jour l entreprise")
    void testUpdateEntreprise() {
        Entreprise updated = Entreprise.builder().id(1L).nom("ESPRIT v2").adresse("Ariana").build();
        when(entrepriseRepository.save(any(Entreprise.class))).thenReturn(updated);
        Entreprise result = entrepriseService.updateEntreprise(updated);
        assertNotNull(result);
        assertEquals("ESPRIT v2", result.getNom());
        verify(entrepriseRepository, times(1)).save(updated);
    }

    @Test
    @DisplayName("deleteEntreprise - doit supprimer l entreprise par ID")
    void testDeleteEntreprise() {
        doNothing().when(entrepriseRepository).deleteById(1L);
        entrepriseService.deleteEntreprise(1L);
        verify(entrepriseRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("getEntrepriseById - doit retourner l entreprise si trouvee")
    void testGetEntrepriseById_Found() {
        when(entrepriseRepository.findById(1L)).thenReturn(Optional.of(entreprise1));
        Entreprise result = entrepriseService.getEntrepriseById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ESPRIT", result.getNom());
        verify(entrepriseRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getEntrepriseById - doit retourner null si non trouvee")
    void testGetEntrepriseById_NotFound() {
        when(entrepriseRepository.findById(anyLong())).thenReturn(Optional.empty());
        Entreprise result = entrepriseService.getEntrepriseById(999L);
        assertNull(result);
        verify(entrepriseRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("getAllEntreprises - doit retourner toutes les entreprises")
    void testGetAllEntreprises() {
        when(entrepriseRepository.findAll()).thenReturn(Arrays.asList(entreprise1, entreprise2));
        List<Entreprise> result = entrepriseService.getAllEntreprises();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ESPRIT", result.get(0).getNom());
        assertEquals("Google", result.get(1).getNom());
        verify(entrepriseRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllEntreprises - doit retourner une liste vide")
    void testGetAllEntreprises_Empty() {
        when(entrepriseRepository.findAll()).thenReturn(Arrays.asList());
        List<Entreprise> result = entrepriseService.getAllEntreprises();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(entrepriseRepository, times(1)).findAll();
    }
}