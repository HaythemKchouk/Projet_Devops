package tn.esprit.spring.kaddem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j // Lombok annotation for automatic logging (Log4j)
public class EtudiantServiceImpl implements IEtudiantService {

	@Autowired
	private EtudiantRepository etudiantRepository;

	@Autowired
	private ContratRepository contratRepository;

	@Autowired
	private EquipeRepository equipeRepository;

	@Autowired
	private DepartementRepository departementRepository;

	// Méthode pour récupérer tous les étudiants
	public List<Etudiant> retrieveAllEtudiants() {
		log.info("Récupération de tous les étudiants."); // Log d'information
		List<Etudiant> etudiants = (List<Etudiant>) etudiantRepository.findAll();
		log.debug("Nombre d'étudiants récupérés: {}", etudiants.size()); // Log de débogage
		return etudiants;
	}

	// Méthode pour ajouter un étudiant
	public Etudiant addEtudiant(Etudiant e) {
		log.info("Ajout d'un nouvel étudiant: {} {}", e.getNomE(), e.getPrenomE()); // Log d'ajout avec les informations de l'étudiant
		Etudiant savedEtudiant = etudiantRepository.save(e); // Enregistrement de l'étudiant
		log.info("Étudiant ajouté avec succès, ID: {}", savedEtudiant.getIdEtudiant()); // Log d'information
		return savedEtudiant;
	}

	// Méthode pour mettre à jour un étudiant existant
	public Etudiant updateEtudiant(Etudiant e) {
		log.info("Mise à jour de l'étudiant avec ID: {}", e.getIdEtudiant()); // Log de mise à jour avec l'ID de l'étudiant
		Etudiant updatedEtudiant = etudiantRepository.save(e); // Mise à jour de l'étudiant
		log.info("Étudiant mis à jour avec succès, ID: {}", updatedEtudiant.getIdEtudiant()); // Log d'information
		return updatedEtudiant;
	}

	// Méthode pour récupérer un étudiant par ID
	public Etudiant retrieveEtudiant(Integer idEtudiant) {
		log.info("Récupération de l'étudiant avec ID: {}", idEtudiant); // Log de récupération
		return etudiantRepository.findById(idEtudiant).orElse(null); // Retourne l'étudiant ou null s'il n'existe pas
	}

	// Méthode pour supprimer un étudiant par ID
	public void removeEtudiant(Integer idEtudiant) {
		log.info("Tentative de suppression de l'étudiant avec ID: {}", idEtudiant); // Log d'information
		Etudiant e = retrieveEtudiant(idEtudiant);
		if (e != null) {
			log.info("Suppression de l'étudiant: {} {}", e.getNomE(), e.getPrenomE()); // Log de suppression avec le nom et prénom de l'étudiant
			etudiantRepository.delete(e); // Suppression de l'étudiant
			log.info("Étudiant supprimé avec succès."); // Log de succès
		} else {
			log.warn("Impossible de supprimer, étudiant introuvable pour l'ID: {}", idEtudiant); // Avertissement si l'étudiant n'est pas trouvé
		}
	}

	// Méthode pour assigner un étudiant à un département
	public void assignEtudiantToDepartement(Integer etudiantId, Integer departementId) {
		log.info("Tentative d'assignation de l'étudiant ID: {} au département ID: {}", etudiantId, departementId); // Log d'information
		Etudiant etudiant = etudiantRepository.findById(etudiantId).orElse(null);
		Departement departement = departementRepository.findById(departementId).orElse(null);

		if (etudiant != null && departement != null) {
			etudiant.setDepartement(departement); // Assignation du département à l'étudiant
			etudiantRepository.save(etudiant); // Enregistrement de l'étudiant avec le département
			log.info("Étudiant {} {} assigné au département: {}", etudiant.getNomE(), etudiant.getPrenomE(), departement.getNomDepart()); // Log d'assignation
		} else {
			log.error("Erreur lors de l'assignation. Étudiant ou département introuvable."); // Log d'erreur en cas d'assignation impossible
		}
	}

	// Méthode pour ajouter et assigner un étudiant à une équipe et un contrat
	@Transactional
	public Etudiant addAndAssignEtudiantToEquipeAndContract(Etudiant e, Integer idContrat, Integer idEquipe) {
		log.info("Ajout et assignation de l'étudiant {} {} au contrat ID: {} et à l'équipe ID: {}", e.getNomE(), e.getPrenomE(), idContrat, idEquipe); // Log d'ajout
		Contrat contrat = contratRepository.findById(idContrat).orElse(null);
		Equipe equipe = equipeRepository.findById(idEquipe).orElse(null);

		if (contrat != null && equipe != null) {
			contrat.setEtudiant(e); // Assignation du contrat à l'étudiant
			equipe.getEtudiants().add(e); // Ajout de l'étudiant à l'équipe
			etudiantRepository.save(e); // Enregistrement de l'étudiant
			log.info("Étudiant {} {} assigné au contrat {} et à l'équipe {}", e.getNomE(), e.getPrenomE(), idContrat, idEquipe); // Log d'assignation
		} else {
			log.error("Erreur lors de l'assignation de l'étudiant au contrat ou à l'équipe (Contrat ID: {}, Équipe ID: {})", idContrat, idEquipe); // Log d'erreur si le contrat ou l'équipe est introuvable
		}
		return e; // Retourne l'étudiant assigné
	}

	// Méthode pour récupérer les étudiants d'un département
	public List<Etudiant> getEtudiantsByDepartement(Integer idDepartement) {
		log.info("Récupération des étudiants pour le département ID: {}", idDepartement); // Log de récupération des étudiants par département
		List<Etudiant> etudiants = etudiantRepository.findEtudiantsByDepartement_IdDepart(idDepartement);
		log.debug("Nombre d'étudiants récupérés pour le département ID {}: {}", idDepartement, etudiants.size()); // Log de débogage
		return etudiants; // Retourne la liste des étudiants par département
	}
}
