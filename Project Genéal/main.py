# Ce code est basé sur les arbres de recherche (comme un Graphe ) 
# Le but est de créé un arbe généalogiue d'une Famille. Chaque personne de la famille doit avoir un : 
# Nom , prénom , date de naissance ,  

import json
import os

FICHIER_JSON_SAUVEGARDE = "arbre_genealogique.json"
    
def personne_to_dict(p):
    return {
        "nom": p.nom,
        "prenom": p.prenom, 
        "dat_nais": p.dat_nais, 
        "enfants": [personne_to_dict(e) for e in p.enfants], 
        "conjoint": p.conjoint.prenom if p.conjoint else None
    }      

def sauvegarde_arbre(racine):
    with open(FICHIER_JSON_SAUVEGARDE, "w",
    encoding="utf-8") as f:
        json.dump(personne_to_dict(racine), 
            f, ensure_ascii=False, indent=2)

def dict_to_personne(d):
    p = Person(d["nom"], d["prenom"], d["dat_nais"])
    p.enfants = [dict_to_personne(e) for e in d.get("enfants", [])]
    return p

def charge_arbre():
    if not os.path.exists(FICHIER_JSON_SAUVEGARDE):
        return None
    with open(FICHIER_JSON_SAUVEGARDE, 'r', encoding="utf-8") as f:
        data = json.load(f)
    return dict_to_personne(data)

class Person:
    def __init__(self, nom, prenom, dat_nais, pere=None, mere=None):
        self.nom = nom
        self.prenom = prenom
        self.dat_nais = dat_nais
        self.pere = pere
        self.mere = mere
        self.enfants = []
        self.conjoint = None
    
    def se_marie (self, partenaire):
        self.conjoint = partenaire
        partenaire.conjoint = self

    def avoir_descendant(self, child, en_tant_que='pere'):
        """Ajoute un enfant et établit la relation parent-enfant."""
        self.enfants.append(child)
        if en_tant_que == 'pere':
            child.pere = self
        elif en_tant_que == 'mere':
            child.mere = self

    def childrens(self, enfants, en_tant_que='pere'):
        """Ajoute plusieurs enfants."""
        for child in enfants:
            self.avoir_descendant(child, en_tant_que)

    def __str__(self):
        return (f"{self.nom}  {self.prenom} né(e) le {self.dat_nais} ")

    def voir_arbre(self, niveau=0):
        indent = " "* niveau
        info = f" {self.prenom}  {self.nom} ({self.dat_nais})"
        if self.conjoint: 
            info += f" est marié(e) à {self.conjoint.nom} {self.conjoint.prenom} ({self.conjoint.dat_nais})"
        print(indent + info )
        for child in self.enfants:
            child.voir_arbre(niveau + 1)

    def search_person(self, nom_parent):
        nom_part = nom_parent.strip().split()
        if len(nom_part) < 2: # cela signifie qu'on ne lance pas la recherche de la person si le nom donné n'atteint pas minimun 2
            return None
        nom,  prenom  = nom_part[0]," ".join(nom_part[1:])
        
        if self.nom.strip().lower() == nom.strip().lower() and self.prenom.strip().lower() == prenom.strip().lower():
            return self
        for enfant in self.enfants:
            result = enfant.search_person(nom_parent)
            if result:
                return result
        return None
    
    def search(self, nom_recherche):
        if self.prenom == nom_recherche:
            print(f" {self.nom} {self.prenom} né(é) le {self.dat_nais}")
            return True
        for enfant in self.enfants:
            if enfant.search(nom_recherche):
                return True
        return False
           

    def mise_a_jour(self):
        """Supprime tous les descendants récursivement."""
        for enfant in self.enfants:
            enfant.mise_a_jour()
        self.enfants = []

# --- Menu de test simple ---
def menu():
    racine = charge_arbre()
    if not racine:
        print("Aucun arbre trouvé, création d’un arbre par défaut.")
        nom_grdPere = input(" Nom du Grand-père :")
        prenom_grdPere = input("Prénoms du Grand-Père : ")
        naiss_grdPere = input("Date de naisssance du Grand-Père : ")
        
        nom_grdmere = input(" Nom de la Grand-mère : ")
        prenom_grdMere = input("Prénom de la Grand-mère : ")
        naiss_grdMere = input ("Date de naissance  de la Grand-mère : ")

    # Création des liens
        grand_pere = Person(nom_grdPere, prenom_grdPere, naiss_grdPere)
        grand_mere = Person(nom_grdmere, prenom_grdMere, naiss_grdMere)
        grand_pere.se_marie(grand_mere)
        racine = grand_pere
        sauvegarde_arbre(racine)

    while True:
        print("\n--- MENU ---")
        print("1. Afficher l’arbre")
        print("2. Mariage")
        print("3. Ajouter un enfant")
        print("4. Rechercher une personne")
        print("5. Réinitialiser l’arbre")
        print("6. Quitter")
        choix = input("Choisissez une option : ")

        if choix == "1":
            print("\n--- Arbre Généalogique ---")
            racine.voir_arbre()

        elif choix == "2":
            epoux = input("Veuillez entrer le Nom complet du marier:")    
            epouse = input("Veuillez entrer le Nom complet de la marier :")
            
            partenaire1 = racine.search_person(epoux)
            partenaire2 = racine.search_person(epouse)

            if not partenaire1 or not partenaire2:
                print(f"L'un des deux partenaire est introuvable.  ")
                continue
            if partenaire1.conjoint  or partenaire2.conjoint: 
                print(f"L'un des deux partenaire est éjà marié .")
                continue

            confirmation = input(f"Confirmez-vous le mariage entre {partenaire1.prenom} et  {partenaire2.prenom} ? (oui/non)").strip().lower()
            if confirmation == "oui":
                partenaire1.se_marie(partenaire2)
                print("Mariage Enrégistré.")
                if input("Souhaitez-vous sauvegarder les modifiacations ? (oui/non)").strip().lower() == "oui": 
                    sauvegarde_arbre(racine)    
                
                else:
                    print("Mariage Annulé .")


        elif choix == "3":
            nom_parent = input("Veuillez entrez le nom complet du parent : ")
            enfant_nom = input("Nom de l’enfant : ")
            enfant_prenom = input("Entrer le prenom de l'enfant : ")
            enfant_date = input("Date de naissance de l’enfant : ")
            role = input("Ce parent est-il le père ou la mère ? (pere/mere) : ")

         
            parent = racine.search_person(nom_parent)
            if parent:
                nouvel_enfant = Person(enfant_nom,enfant_prenom, enfant_date)
                parent.avoir_descendant(nouvel_enfant, en_tant_que=role)
                print(f"{enfant_prenom} {enfant_nom}  a été ajouté(e) comme enfant de {nom_parent}")
                if input("Souhaitez-vous  sauvegarder les modifications ? (oui/non) : ").strip().lower()  == "oui":
                    sauvegarde_arbre(racine)
            else:
                print("Parent introuvable.")
        
        elif choix == "4":  # Retester la recherche de personne  dans l'arbre (methode search) 
            prenom = input("Entrer le prénom de l'individu que vous recherchez : ")
            
            #personne = racine.search(nom_recherche)
            #if  personne:
                #personne.search(nom_recherche) 
            if not racine.search_person(prenom):
                print("Personne  trouvée.")
            else: 
                print("Personne non Trouvé")

        elif choix == "5":
            confirmation = input("Voulez-vous vraiment tout effacer ? (oui/non) : ")
            if confirmation == "oui":
                racine.mise_a_jour()
                print("Arbre réinitialisé.")
                if input("Souhaitez- vous sauvegarder les modifications apporter (oui/non) :").strip().lower() == "oui":
                    sauvegarde_arbre(racine)

        elif choix == "6":
            print("Fin du programme.")
            break

        else:
            print("Choix invalide.")
if __name__ == "__main__":

    menu()
