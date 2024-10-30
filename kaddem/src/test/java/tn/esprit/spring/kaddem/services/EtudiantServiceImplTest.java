package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EtudiantServiceImplTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private ContratRepository contratRepository;

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private DepartementRepository departementRepository;

    @InjectMocks
    private EtudiantServiceImpl etudiantServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRetrieveAllEtudiants() {
        // Préparer les données
        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(new Etudiant("Bouzid", "Arij"));
        etudiants.add(new Etudiant("Miss", "Miss"));

        // Simuler la méthode du repository
        when(etudiantRepository.findAll()).thenReturn(etudiants);

        // Appeler la méthode à tester
        List<Etudiant> result = etudiantServiceImpl.retrieveAllEtudiants();

        // Vérifier le résultat
        assertEquals(2, result.size());
        verify(etudiantRepository, times(1)).findAll();
    }

    @Test
    public void testAddEtudiant() {
        // Créer un exemple d'étudiant
        Etudiant etudiant = new Etudiant("TestNom", "TestPrenom");

        // Simuler le comportement du repository
        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        // Appeler la méthode à tester
        Etudiant result = etudiantServiceImpl.addEtudiant(etudiant);

        // Vérifier le résultat
        assertEquals("TestNom", result.getNomE());
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    public void testUpdateEtudiant() {
        // Créer un exemple d'étudiant
        Etudiant etudiant = new Etudiant("TestNom", "TestPrenom");
        etudiant.setIdEtudiant(1);

        // Simuler le comportement du repository
        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        // Appeler la méthode à tester
        Etudiant result = etudiantServiceImpl.updateEtudiant(etudiant);

        // Vérifier le résultat
        assertEquals(1, result.getIdEtudiant());
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    public void testRetrieveEtudiant() {
        // Créer un exemple d'étudiant
        Etudiant etudiant = new Etudiant("TestNom", "TestPrenom");
        etudiant.setIdEtudiant(1);

        // Simuler la méthode du repository
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));

        // Appeler la méthode à tester
        Etudiant result = etudiantServiceImpl.retrieveEtudiant(1);

        // Vérifier le résultat
        assertNotNull(result);
        assertEquals(1, result.getIdEtudiant());
        verify(etudiantRepository, times(1)).findById(1);
    }

    @Test
    public void testRemoveEtudiant() {
        // Créer un exemple d'étudiant
        Etudiant etudiant = new Etudiant("TestNom", "TestPrenom");
        etudiant.setIdEtudiant(1);

        // Simuler la méthode du repository
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));

        // Appeler la méthode à tester
        etudiantServiceImpl.removeEtudiant(1);

        // Vérifier les interactions
        verify(etudiantRepository, times(1)).delete(etudiant);
    }

    @Test
    public void testAssignEtudiantToDepartement() {
        // Créer des exemples d'étudiant et de département
        Etudiant etudiant = new Etudiant("TestNom", "TestPrenom");
        etudiant.setIdEtudiant(1);

        Departement departement = new Departement();
        departement.setIdDepart(1);

        // Simuler les méthodes du repository
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));
        when(departementRepository.findById(1)).thenReturn(Optional.of(departement));

        // Appeler la méthode à tester
        etudiantServiceImpl.assignEtudiantToDepartement(1, 1);

        // Vérifier que l'étudiant a bien été assigné
        assertEquals(departement, etudiant.getDepartement());
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    public void testAddAndAssignEtudiantToEquipeAndContract() {
        // Créer des exemples d'étudiant, d'équipe et de contrat
        Etudiant etudiant = new Etudiant("TestNom", "TestPrenom");
        etudiant.setIdEtudiant(1);

        Contrat contrat = new Contrat();
        contrat.setIdContrat(1);

        Equipe equipe = new Equipe();
        equipe.setIdEquipe(1);

        // Simuler les méthodes du repository
        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));

        // Appeler la méthode à tester
        etudiantServiceImpl.addAndAssignEtudiantToEquipeAndContract(etudiant, 1, 1);

        // Vérifier les assignations
        assertEquals(etudiant, contrat.getEtudiant());
        assertTrue(equipe.getEtudiants().contains(etudiant));
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    public void testGetEtudiantsByDepartement() {
        // Créer des exemples d'étudiants
        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(new Etudiant("Nom1", "Prenom1"));

        // Simuler la méthode du repository
        when(etudiantRepository.findEtudiantsByDepartement_IdDepart(1)).thenReturn(etudiants);

        // Appeler la méthode à tester
        List<Etudiant> result = etudiantServiceImpl.getEtudiantsByDepartement(1);

        // Vérifier le résultat
        assertEquals(1, result.size());
        verify(etudiantRepository, times(1)).findEtudiantsByDepartement_IdDepart(1);
    }
}
