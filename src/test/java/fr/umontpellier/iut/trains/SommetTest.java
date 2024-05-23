package fr.umontpellier.iut.trains;

import fr.umontpellier.iut.graphes.Graphe;
import fr.umontpellier.iut.trains.plateau.Plateau;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SommetTest {
    @Test
    public void test_constructeur_avec_tuile_et_jeu() {
        Jeu jeu = new Jeu(new String[]{"Alexis", "Jean-Marc", "Robert"}, new String[]{}, Plateau.OSAKA);
        Graphe graphe = jeu.getGraphe();
        jeu.getTuile(0).ajouterRail(jeu.getJoueurs().get(1));
        jeu.getTuile(2).ajouterRail(jeu.getJoueurs().get(2));
        jeu.getTuile(2).ajouterRail(jeu.getJoueurs().get(0));

        Set<Integer> rails_tuile1 = new HashSet<>(List.of(1));
        Set<Integer> rails_tuile3 = new HashSet<>(Arrays.asList(0, 2));
        assertEquals(rails_tuile1, graphe.getSommet(0).getJoueurs());
        assertEquals(rails_tuile3, graphe.getSommet(2).getJoueurs());
    }
}
