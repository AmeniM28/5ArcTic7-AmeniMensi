package tn.esprit.backend.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.backend.entity.Projet;
import tn.esprit.backend.repository.ProjetRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjetServiceTest {

    @Mock
    private ProjetRepository projetRepository;

    @InjectMocks
    private ProjetServiceImpl projetService;

    private Projet projet1;
    private Projet projet2;

    @BeforeEach
    void setUp() {
        projet1 = Projet.builder().id(1L).sujet("Projet DevOps").build();
        projet2 = Projet.builder().id(2L).sujet("Projet Angular").build();
    }

    @Test
    void testAddProjet() {
        when(projetRepository.save(any(Projet.class))).thenReturn(projet1);
        Projet result = projetService.addProjet(projet1);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(projetRepository, times(1)).save(projet1);
    }

    @Test
    void testUpdateProjet() {
        Projet updated = Projet.builder().id(1L).sujet("Projet DevOps v2").build();
        when(projetRepository.save(any(Projet.class))).thenReturn(updated);
        Projet result = projetService.updateProjet(updated);
        assertNotNull(result);
        verify(projetRepository, times(1)).save(updated);
    }

    @Test
    void testDeleteProjet() {
        doNothing().when(projetRepository).deleteById(1L);
        projetService.deleteProjet(1L);
        verify(projetRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetProjetById_Found() {
        when(projetRepository.findById(1L)).thenReturn(Optional.of(projet1));
        Projet result = projetService.getProjetById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetProjetById_NotFound() {
        when(projetRepository.findById(anyLong())).thenReturn(Optional.empty());
        Projet result = projetService.getProjetById(999L);
        assertNull(result);
    }

    @Test
    void testGetAllProjets() {
        when(projetRepository.findAll()).thenReturn(Arrays.asList(projet1, projet2));
        List<Projet> result = projetService.getAllProjets();
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllProjets_Empty() {
        when(projetRepository.findAll()).thenReturn(Arrays.asList());
        List<Projet> result = projetService.getAllProjets();
        assertTrue(result.isEmpty());
    }
}