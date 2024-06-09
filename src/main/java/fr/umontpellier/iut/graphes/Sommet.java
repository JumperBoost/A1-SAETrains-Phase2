package fr.umontpellier.iut.graphes;

import fr.umontpellier.iut.trains.Jeu;
import fr.umontpellier.iut.trains.Joueur;
import fr.umontpellier.iut.trains.plateau.Tuile;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Classe modélisant les sommets. Le numéro du sommet correspond à la numérotation du plateau en partant
 * d'en haut à gauche et en allant vers le bas à droite.
 */
public class Sommet {
    /**
     * Numéro du sommet.
     */
    private final int i;
    /**
     * Coût de pose d'un rail sur la tuile correspondante.
     */
    private final int surcout;
    /**
     * Nombre de points de victoire que rapporte la tuile si un joueur a un rail dessus
     */
    private int nbPointsVictoire;
    /**
     * Ensemble des joueurs ayant un rail sur la tuile.
     */
    private Set<Integer> joueurs;
    /**
     * Ensemble des sommets voisins.
     */
    private Set<Sommet> voisins;

    /**
     * Constructeur privé pour forcer l'utilisation du builder.
     */
    private Sommet(int i, int surcout, Set<Integer> joueurs, int nbPointsVictoire) {
        this.i = i;
        this.surcout = surcout;
        this.joueurs = joueurs;
        this.nbPointsVictoire = nbPointsVictoire;
        this.voisins = new HashSet<>();
    }


    public Sommet(Tuile tuile, Jeu jeu) {
        this.i = jeu.getTuiles().indexOf(tuile);
        this.surcout = tuile.getSurcout();
        this.joueurs = jeu.getJoueurs().stream().filter(tuile::hasRail).map(joueur -> jeu.getJoueurs().indexOf(joueur)).collect(Collectors.toSet());
        this.nbPointsVictoire = tuile.getNbPointsVictoire();
        this.voisins = new HashSet<>();
    }

    /**
     * Constructeur par recopie.
     * @param s
     */
    public Sommet(Sommet s){
        this.i = s.i;
        this.surcout = s.surcout;
        this.nbPointsVictoire = s.nbPointsVictoire;
        this.joueurs = new HashSet<>(s.joueurs);
        this.voisins = new HashSet<>(s.voisins);
    }

    public int getIndice() {
        return i;
    }

    public void ajouterJoueur(Joueur joueur) {
        joueurs.add(joueur.getJeu().getJoueurs().indexOf(joueur));
    }

    public Set<Integer> getJoueurs() {
        return joueurs;
    }

    public int getNbPointsVictoire() {
        return nbPointsVictoire;
    }

    /**
     * @return le coût de pose d'un rail sur la tuile correspondante.
     * Les effets des cartes du jeu ne sont pas à prendre en compte.
     */
    public int getSurcout() {
        return surcout;
    }

    public Set<Sommet> getVoisins() {
        return voisins;
    }

    public void ajouterVoisin(Sommet voisin) {
        if(voisin != null && voisin != this)
            voisins.add(voisin);
    }

    /**
     * Supprime le sommet voisin du sommet courant
     * @param s un sommet à retirer des voisins de this
     */
    public void supprimerVoisin(Sommet s){ voisins.remove(s); }

    public boolean estVoisin(Sommet sommet) {
        return voisins.contains(sommet);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sommet sommet)) return false;
        return i == sommet.i;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(i);
    }

    @Override
    public String toString() {
        return String.valueOf(i);
    }

    public static class SommetBuilder {
        private int i;
        private int surcout = 0;
        private int nbPointsVictoire = 0;
        private Set<Integer> joueurs = new HashSet<>();

        public SommetBuilder setIndice(int i) {
            this.i = i;
            return this;
        }

        public SommetBuilder setJoueurs(Set<Integer> joueurs) {
            this.joueurs = joueurs;
            return this;
        }

        public SommetBuilder setSurcout(int surcout) {
            this.surcout = surcout;
            return this;
        }

        public SommetBuilder setNbPointsVictoire(int nbPointsVictoire) {
            this.nbPointsVictoire = nbPointsVictoire;
            return this;
        }

        public Sommet createSommet() {
            return new Sommet(i, surcout, joueurs, nbPointsVictoire);
        }
    }
}
