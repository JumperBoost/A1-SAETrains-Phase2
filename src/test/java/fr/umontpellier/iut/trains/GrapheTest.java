package fr.umontpellier.iut.trains;

import fr.umontpellier.iut.graphes.Sommet;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Timeout;

import fr.umontpellier.iut.graphes.Graphe;
import fr.umontpellier.iut.trains.plateau.Plateau;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Timeout(value = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class GrapheTest {

    private Graphe getGrapheInduit() {
        Graphe graphe = new Graphe(0);
        graphe.ajouterSommet(new Sommet.SommetBuilder().setIndice(0).setSurcout(10).setNbPointsVictoire(2).setJoueurs(new HashSet<>(List.of(0, 1))).createSommet());
        graphe.ajouterSommet(new Sommet.SommetBuilder().setIndice(1).setSurcout(12).setNbPointsVictoire(4).setJoueurs(new HashSet<>(List.of(1, 2))).createSommet());
        graphe.ajouterSommet(new Sommet.SommetBuilder().setIndice(2).setSurcout(14).setNbPointsVictoire(6).setJoueurs(new HashSet<>(List.of(1, 2))).createSommet());
        graphe.ajouterSommet(new Sommet.SommetBuilder().setIndice(3).setSurcout(16).setNbPointsVictoire(8).setJoueurs(new HashSet<>(List.of(0, 2))).createSommet());
        graphe.ajouterSommet(new Sommet.SommetBuilder().setIndice(4).setSurcout(18).setNbPointsVictoire(10).setJoueurs(new HashSet<>(List.of(0, 1))).createSommet());

        graphe.ajouterArete(graphe.getSommet(0), graphe.getSommet(1));
        graphe.ajouterArete(graphe.getSommet(0), graphe.getSommet(2));
        graphe.ajouterArete(graphe.getSommet(3), graphe.getSommet(4));
        return graphe;
    }

    @Test
    public void test_creer_graphe_induit() {
        Graphe graphe = getGrapheInduit();
        Set<Sommet> sommets = new HashSet<>();
        sommets.add(graphe.getSommet(0));
        sommets.add(graphe.getSommet(1));
        sommets.add(graphe.getSommet(2));
        sommets.add(graphe.getSommet(3));
        Graphe g = new Graphe(graphe, sommets);

        assertEquals(4, g.getNbSommets());
        assertEquals(2, g.getNbAretes());
        for(Sommet sommet : g.getSommets()) {
            Sommet s = graphe.getSommet(sommet.getIndice());
            assertEquals(s, sommet);
            assertEquals(s.getSurcout(), sommet.getSurcout());
            assertEquals(s.getNbPointsVictoire(), sommet.getNbPointsVictoire());
            assertEquals(s.getJoueurs(), sommet.getJoueurs());
        }
        assertEquals(new HashSet<Sommet>(), g.getSommet(3).getVoisins());
    }

    @Test
    public void test_creer_graphe_induit_avec_donnees_sommets_different() {
        Graphe graphe = getGrapheInduit();
        Set<Sommet> sommets = new HashSet<>();
        Sommet sommet0 = new Sommet.SommetBuilder().setIndice(0).setSurcout(10).setNbPointsVictoire(2).setJoueurs(new HashSet<>(List.of(0, 1))).createSommet();
        Sommet sommet1 = new Sommet.SommetBuilder().setIndice(1).setSurcout(8).setNbPointsVictoire(0).setJoueurs(new HashSet<>(List.of(0, 2))).createSommet();
        Sommet sommet2 = new Sommet.SommetBuilder().setIndice(2).setSurcout(20).setNbPointsVictoire(10).setJoueurs(new HashSet<>()).createSommet();
        Sommet sommet3 = new Sommet.SommetBuilder().setIndice(3).setSurcout(10).setNbPointsVictoire(6).setJoueurs(new HashSet<>(List.of(1, 2))).createSommet();
        Sommet sommet4 = new Sommet.SommetBuilder().setIndice(4).setSurcout(5).setNbPointsVictoire(8).setJoueurs(new HashSet<>(List.of(1, 2))).createSommet();
        sommets.add(sommet0);
        sommets.add(sommet1);
        sommets.add(sommet2);
        sommets.add(sommet3);
        sommet0.ajouterVoisin(sommet1);
        sommet0.ajouterVoisin(sommet2);
        sommet3.ajouterVoisin(sommet4);
        Graphe g = new Graphe(graphe, sommets);

        assertEquals(4, g.getNbSommets());
        assertEquals(2, g.getNbAretes());
        for(Sommet sommet : g.getSommets()) {
            Sommet s = graphe.getSommet(sommet.getIndice());
            assertEquals(s, sommet);
            assertEquals(s.getSurcout(), sommet.getSurcout());
            assertEquals(s.getNbPointsVictoire(), sommet.getNbPointsVictoire());
            assertEquals(s.getJoueurs(), sommet.getJoueurs());
        }
        assertEquals(new HashSet<Sommet>(), g.getSommet(3).getVoisins());
    }

    @Test
    public void test_graphe_tokyo() {
        Jeu jeu = new Jeu(new String[]{"Batman", "Robin"}, new String[]{}, Plateau.TOKYO);
        Graphe graphe = jeu.getGraphe();

        assertEquals(66, graphe.getNbSommets());
        assertEquals(156, graphe.getNbAretes());
        assertTrue(graphe.estConnexe());
        assertTrue(graphe.possedeUnCycle());
        assertEquals(6, graphe.degreMax());
    }


    @Test
    public void test_distances_tokyo() {
        Jeu jeu = new Jeu(new String[]{"Rick", "Morty"}, new String[]{}, Plateau.TOKYO);
        Graphe graphe = jeu.getGraphe();

        assertEquals(4, graphe.getDistance(graphe.getSommet(0), graphe.getSommet(54)));
        assertEquals(0, graphe.getDistance(graphe.getSommet(13), graphe.getSommet(54)));
        assertEquals(0, graphe.getDistance(graphe.getSommet(3), graphe.getSommet(54)));
        assertEquals(11, graphe.getDistance(graphe.getSommet(67), graphe.getSommet(9)));
        assertEquals(2, graphe.getDistance(graphe.getSommet(34), graphe.getSommet(35)));
    }

    @Test
    public void test_graphe_osaka() {
        Jeu jeu = new Jeu(new String[]{"Lois", "Clark"}, new String[]{}, Plateau.OSAKA);
        Graphe graphe = jeu.getGraphe();

        assertEquals(66, graphe.getNbSommets());
        assertEquals(151, graphe.getNbAretes());
        assertTrue(graphe.estConnexe());
        assertTrue(graphe.possedeUnCycle());
        assertEquals(6, graphe.degreMax());
    }

    @Test
    public void est_complet_vrai() {
        Graphe graphe = new Graphe(20);
        int nbvoisin = 0;
        for (int n = 0; n < graphe.getNbSommets()-1; n++) {
            for (int x = n+1; x < graphe.getNbSommets(); x++) {
                if (!graphe.getSommet(n).estVoisin(graphe.getSommet(x))){
                    graphe.ajouterArete(graphe.getSommet(n), graphe.getSommet(x));
                    nbvoisin++;
                }
            }
        }

        assertEquals(nbvoisin, graphe.getNbAretes());
        assertTrue(graphe.estComplet());
    }

    @Test
    public void est_chaine_vrai() {
        Graphe graphe = new Graphe(15);
        int nbArrete = 0;
        for (int i=1; i<graphe.getNbSommets(); i++){
            graphe.ajouterArete(graphe.getSommet(i-1), graphe.getSommet(i));
            nbArrete++;
        }

        assertEquals(nbArrete, graphe.getNbAretes());
        assertTrue(graphe.estChaine());
    }

    @Test
    public void est_chaine_vrai_avec_un_sommet_pas_bon_ordre() {
        Graphe graphe = new Graphe(15);
        for (int i=1; i<graphe.getNbSommets()-1; i++)
            graphe.ajouterArete(graphe.getSommet(i-1), graphe.getSommet(i));
        graphe.ajouterArete(graphe.getSommet(0), graphe.getSommet(graphe.getNbSommets()-1));

        assertTrue(graphe.estChaine());
    }

    @Test
    public void est_chaine_faux() {
        Jeu jeu = new Jeu(new String[]{"Lois", "Clark"}, new String[]{}, Plateau.OSAKA);

        Graphe graphe = jeu.getGraphe();
        for (int i=1; i<graphe.getNbSommets(); i++)
            graphe.ajouterArete(graphe.getSommet(i-1), graphe.getSommet(i));
        graphe.ajouterArete(graphe.getSommet(0), graphe.getSommet(3));

        assertFalse(graphe.estChaine());
    }

    @Test
    public void est_chaine_faux_avec_un_sommet_isole() {
        Jeu jeu = new Jeu(new String[]{"Lois", "Clark"}, new String[]{}, Plateau.OSAKA);

        Graphe graphe = jeu.getGraphe();
        for (int i=1; i<graphe.getNbSommets()-1; i++)
            graphe.ajouterArete(graphe.getSommet(i-1), graphe.getSommet(i));

        assertFalse(graphe.estChaine());
    }

    @Test
    public void est_chaine_faux_avec_un_sommet_isole_et_cycle() {
        Jeu jeu = new Jeu(new String[]{"Lois", "Clark"}, new String[]{}, Plateau.OSAKA);

        Graphe graphe = jeu.getGraphe();
        for (int i=1; i<graphe.getNbSommets()-1; i++)
            graphe.ajouterArete(graphe.getSommet(i-1), graphe.getSommet(i));
        graphe.ajouterArete(graphe.getSommet(0), graphe.getSommet(graphe.getNbSommets()-2));

        assertFalse(graphe.estChaine());
    }

    @Test
    public void est_connexe_vrai() {
        Jeu jeu = new Jeu(new String[]{"Lois", "Clark"}, new String[]{}, Plateau.OSAKA);

        Graphe graphe = jeu.getGraphe();
        for (int i=0; i<graphe.getNbSommets(); i++){
            if (!(i==0)){
                graphe.ajouterArete(graphe.getSommet(i-1), graphe.getSommet(i));
            }
        }

        assertTrue(graphe.estConnexe());
    }

    @Test
    public void est_connexe_vrai_et_complet() {
        Jeu jeu = new Jeu(new String[]{"Lois", "Clark"}, new String[]{}, Plateau.OSAKA);

        Graphe graphe = jeu.getGraphe();
        for (int i=0; i<graphe.getSommets().size(); i++){
            if (graphe.getNbSommets()>1) {
                for (int n = 0; n < graphe.getNbSommets()-1; n++) {
                    for (int x = n+1; x < graphe.getNbSommets(); x++) {
                        if (!graphe.getSommet(n).estVoisin(graphe.getSommet(x))){
                            graphe.ajouterArete(graphe.getSommet(n), graphe.getSommet(x));
                        }
                    }
                }
            }
        }

        assertTrue(graphe.estConnexe());
    }

    @Test
    public void est_connexe_faux() {
        Graphe graphe = new Graphe(15);
        for (int i=0; i<graphe.getNbSommets(); i++){
            if (!(i==0)){
                graphe.ajouterArete(graphe.getSommet(i-1), graphe.getSommet(i));
            }
        }
        assertTrue(graphe.estChaine());
        graphe.supprimerArete(graphe.getSommet(2), graphe.getSommet(3));

        assertFalse(graphe.estChaine());
        assertFalse(graphe.estConnexe());
    }

    @Test
    public void est_connexe_faux_un_sommet_isole() {
        Jeu jeu = new Jeu(new String[]{"Lois", "Clark"}, new String[]{}, Plateau.OSAKA);

        Graphe graphe = jeu.getGraphe();
        for (int i=0; i<graphe.getNbSommets(); i++){
            if (!(i==0)){
                graphe.ajouterArete(graphe.getSommet(i-1), graphe.getSommet(i));
            }
        }
        graphe.supprimerArete(graphe.getSommet(0), graphe.getSommet(1));

        assertFalse(graphe.estConnexe());
    }

    @Test
    public void est_connexe_faux_un_sommet_isole_mais_les_autres_tous_voisin() {
        Jeu jeu = new Jeu(new String[]{"Lois", "Clark"}, new String[]{}, Plateau.OSAKA);

        Graphe graphe = jeu.getGraphe();
        for (int i=0; i<graphe.getNbSommets(); i++){
            if (graphe.getNbSommets()>1) {
                for (int n = 0; n < graphe.getNbSommets()-1; n++) {
                    for (int x = n+1; x < graphe.getNbSommets(); x++) {
                        if (!graphe.getSommet(n).estVoisin(graphe.getSommet(x))){
                            graphe.ajouterArete(graphe.getSommet(n), graphe.getSommet(x));
                        }
                    }
                }
            }
        }
        for (int i=1; i<graphe.getNbSommets(); i++) {
            graphe.supprimerArete(graphe.getSommet(0), graphe.getSommet(i));
        }

        assertFalse(graphe.estConnexe());
    }

    private Graphe generer_au_moins_un_cycle() {
        Graphe graphe = new Graphe(15);
        for(int i = 0; i < 9; i++)
            graphe.ajouterArete(graphe.getSommet(i), graphe.getSommet(i+1));
        graphe.ajouterArete(graphe.getSommet(3), graphe.getSommet(13));
        graphe.ajouterArete(graphe.getSommet(4), graphe.getSommet(14));
        graphe.ajouterArete(graphe.getSommet(13), graphe.getSommet(14));
        return graphe;
    }

    @Test
    public void au_moins_un_cycle_vrai() {
        Graphe graphe = generer_au_moins_un_cycle();
        assertTrue(graphe.possedeUnCycle());
    }

    @Test
    public void au_moins_un_cycle_faux() {
        Graphe graphe = generer_au_moins_un_cycle();
        graphe.supprimerArete(graphe.getSommet(13), graphe.getSommet(14));
        assertFalse(graphe.possedeUnCycle());
    }

    @Test
    public void est_un_cycle_vrai() {
        Graphe graphe = new Graphe(5);
        for(int i = 0; i < 4; i++) {
            graphe.ajouterArete(graphe.getSommet(i), graphe.getSommet(i+1));
        }
        graphe.ajouterArete(graphe.getSommet(0), graphe.getSommet(4));
        assertTrue(graphe.estCycle());
    }

    @Test
    public void est_un_cycle_faux_un() {
        Graphe graphe = new Graphe(5);
        for(int i = 0; i < 4; i++) {
            graphe.ajouterArete(graphe.getSommet(i), graphe.getSommet(i+1));
        }
        graphe.ajouterArete(graphe.getSommet(0), graphe.getSommet(4));
        graphe.ajouterArete(graphe.getSommet(2), graphe.getSommet(4));
        assertFalse(graphe.estCycle());
    }

    @Test
    public void est_un_cycle_faux_deux() {
        Graphe graphe = generer_au_moins_un_cycle();
        assertFalse(graphe.estCycle());
    }

    @Test
    public void sequence_correcte() {
        List<Integer> sequence = new ArrayList<>(List.of(1, 2, 2, 0, 3));

        assertTrue(Graphe.sequenceEstGraphe(sequence));
    }

    @Test
    public void sequence_incorrecte() {
        List<Integer> sequence = new ArrayList<>(List.of(1, 1, 2, 0, 3));

        assertFalse(Graphe.sequenceEstGraphe(sequence));
    }

    @Test
    public void sequence_incorrecte2() {
        List<Integer> sequence = new ArrayList<>(List.of(0, 0, 0, 0, 0));

        assertFalse(Graphe.sequenceEstGraphe(sequence));
    }

    @Test
    public void possede_un_isthme() {
        Jeu jeu = new Jeu(new String[]{"Rick", "Morty"}, new String[]{}, Plateau.TOKYO);
        Graphe graphe = jeu.getGraphe();

        assertTrue(graphe.possedeUnIsthme());
    }

    @Test
    public void possede_un_isthme2() {
        Graphe graphe = new Graphe(6);
        for(int i = 1; i < 4; i++)
            graphe.ajouterArete(graphe.getSommet(i-1), graphe.getSommet(i));
        graphe.ajouterArete(graphe.getSommet(0), graphe.getSommet(4));
        graphe.ajouterArete(graphe.getSommet(5), graphe.getSommet(3));

        assertTrue(graphe.possedeUnIsthme());
    }

    @Test
    public void possede_aucun_isthme() {
        Graphe graphe = new Graphe(6);
        for(int i = 1; i < 4; i++)
            graphe.ajouterArete(graphe.getSommet(i-1), graphe.getSommet(i));
        graphe.ajouterArete(graphe.getSommet(0), graphe.getSommet(4));
        graphe.ajouterArete(graphe.getSommet(4), graphe.getSommet(5));
        graphe.ajouterArete(graphe.getSommet(5), graphe.getSommet(3));

        assertFalse(graphe.possedeUnIsthme());
    }

    @Test
    public void est_un_arbre() {
        Graphe graphe = new Graphe(8);
        for(int i = 3; i < 6; i++)
            graphe.ajouterArete(graphe.getSommet(i-1), graphe.getSommet(i));
        graphe.ajouterArete(graphe.getSommet(0), graphe.getSommet(2));
        graphe.ajouterArete(graphe.getSommet(1), graphe.getSommet(2));
        graphe.ajouterArete(graphe.getSommet(5), graphe.getSommet(6));
        graphe.ajouterArete(graphe.getSommet(5), graphe.getSommet(7));

        assertTrue(graphe.estArbre());
    }

    @Test
    public void est_un_arbre_graphe_vide() {
        Graphe graphe = new Graphe(0);

        assertTrue(graphe.estArbre());
    }

    @Test
    public void est_pas_un_arbre_connexe() {
        Graphe graphe = new Graphe(8);
        for(int i = 3; i < 6; i++)
            graphe.ajouterArete(graphe.getSommet(i-1), graphe.getSommet(i));
        graphe.ajouterArete(graphe.getSommet(0), graphe.getSommet(2));
        graphe.ajouterArete(graphe.getSommet(1), graphe.getSommet(2));
        graphe.ajouterArete(graphe.getSommet(5), graphe.getSommet(7));

        assertFalse(graphe.estArbre());
    }

    @Test
    public void est_pas_un_arbre_sans_cycle() {
        Graphe graphe = new Graphe(8);
        for(int i = 3; i < 6; i++)
            graphe.ajouterArete(graphe.getSommet(i-1), graphe.getSommet(i));
        graphe.ajouterArete(graphe.getSommet(0), graphe.getSommet(2));
        graphe.ajouterArete(graphe.getSommet(1), graphe.getSommet(2));
        graphe.ajouterArete(graphe.getSommet(5), graphe.getSommet(6));
        graphe.ajouterArete(graphe.getSommet(5), graphe.getSommet(7));
        graphe.ajouterArete(graphe.getSommet(0), graphe.getSommet(7));

        assertFalse(graphe.estArbre());
    }

    @Test
    public void est_une_foret() {
        Graphe graphe = new Graphe(16);
        // Arbre 1
        for(int i = 3; i < 6; i++)
            graphe.ajouterArete(graphe.getSommet(i-1), graphe.getSommet(i));
        graphe.ajouterArete(graphe.getSommet(0), graphe.getSommet(2));
        graphe.ajouterArete(graphe.getSommet(1), graphe.getSommet(2));
        graphe.ajouterArete(graphe.getSommet(5), graphe.getSommet(6));
        graphe.ajouterArete(graphe.getSommet(5), graphe.getSommet(7));
        // Arbre 2
        for(int i = 11; i < 14; i++)
            graphe.ajouterArete(graphe.getSommet(i-1), graphe.getSommet(i));
        graphe.ajouterArete(graphe.getSommet(8), graphe.getSommet(10));
        graphe.ajouterArete(graphe.getSommet(9), graphe.getSommet(10));
        graphe.ajouterArete(graphe.getSommet(13), graphe.getSommet(14));
        graphe.ajouterArete(graphe.getSommet(13), graphe.getSommet(15));

        assertTrue(graphe.estForet());
    }







    Graphe g;

    public void initVide() {
        g = new Graphe();
    }

    public void initSommet(int i) {
        g = new Graphe(i);
    }

    public void relierUnSommetATous(Sommet s){
        for (Sommet v : g.getSommets()) {
            s.ajouterVoisin(v);
        }
    }

    public void relierAllSommets() {
        for (Sommet s : g.getSommets()) {
            relierUnSommetATous(s);
        }
    }

    public void initChaine(int i) {
        initSommet(i);
        for (int j = 0; j < i-1; j++) {
            g.getSommet(j).ajouterVoisin(g.getSommet(j+1));
        }
    }

    public void initCycle(int i) {
        if (i > 2) {
            initSommet(i);
            for (int j = 0; j < i-1; j++) {
                g.getSommet(j).ajouterVoisin(g.getSommet(j+1));
            }
            g.getSommet(i-1).ajouterVoisin(g.getSommet(0));
        }
    }

    public List<Integer> ajouterChaineNonReliee(int i) {
        List<Integer> sIndiceAdd = new ArrayList<>();
        int offset = 777;
        for (int j = 0; j < i; j++) {
            while (!g.ajouterSommet(j+offset)) {
                offset+=10;
            }
            sIndiceAdd.add(j+offset);
        }
        for (int j = 0; j < i -1; j++) {
            g.getSommet(j+offset).ajouterVoisin(g.getSommet(j+offset+1));
        }
        return sIndiceAdd;
    }
    public List<Integer> ajouterCycleNonReliee(int i) {
        List<Integer> sIndiceAdd = new ArrayList<>();
        if (i > 2) {
            int offset = 777;
            for (int j = 0; j < i; j++) {
                while (!g.ajouterSommet(j+offset)) {
                    offset+=10;
                }
                sIndiceAdd.add(j+offset);
            }
            for (int j = 0; j < i -1; j++) {
                g.getSommet(j+offset).ajouterVoisin(g.getSommet(j+offset+1));
            }
            g.getSommet(offset).ajouterVoisin(g.getSommet(offset+i-1));
        }
        return sIndiceAdd;
    }

    public void ajouterArbreNonReliee(int longueurTronc, int longueurBranche) {
        //Tronc de longueur longueurTronc et dont chaque branche et reliee à une chqine de longeurBranche
        List<Integer> indiceTronc = ajouterChaineNonReliee(longueurTronc);
        for (Integer indice : indiceTronc) {
            List<Integer> indiceBranche = ajouterChaineNonReliee(longueurBranche);
            g.getSommet(indice).ajouterVoisin(g.getSommet(indiceBranche.get(0)));
        }
    }

    @Disabled
    @Test
    public void test_est_arbre_true_vide() {
        initVide();

        assertTrue(g.estArbre());
    }

    // @Disabled
    @Test
    public void test_est_arbre_true_graine() {
        initSommet(1);

        assertTrue(g.estArbre());
    }

    // @Disabled
    @Test
    public void test_est_arbre_true_chaine() {
        initChaine(3);

        assertTrue(g.estArbre());
    }

    // @Disabled
    @Test
    public void test_est_arbre_true_arbre() {
        initVide();
        ajouterArbreNonReliee(3, 2);

        assertTrue(g.estArbre());
    }

    // @Disabled
    @Test
    public void test_est_arbre_false_non_connexe() {
        initChaine(3);
        ajouterChaineNonReliee(4);
        ajouterChaineNonReliee(1);

        assertFalse(g.estArbre());
    }

    // @Disabled
    @Test
    public void test_est_arbre_false_non_connexe_bis() {
        initVide();
        ajouterArbreNonReliee(3, 4);
        ajouterChaineNonReliee(1);

        assertFalse(g.estArbre());
    }

    // @Disabled
    @Test
    public void test_est_arbre_false_cycle() {
        initCycle(33);

        assertFalse(g.estArbre());
    }

    @Disabled
    @Test
    public void test_fusionnerEnsembleSommets(){
        Graphe graphe = new Graphe(0);
        graphe.ajouterSommet(new Sommet.SommetBuilder().setIndice(0).setSurcout(10).setNbPointsVictoire(2).setJoueurs(new HashSet<>(List.of(0, 1))).createSommet());
        graphe.ajouterSommet(new Sommet.SommetBuilder().setIndice(1).setSurcout(12).setNbPointsVictoire(4).setJoueurs(new HashSet<>(List.of(1, 2))).createSommet());
        graphe.ajouterSommet(new Sommet.SommetBuilder().setIndice(2).setSurcout(14).setNbPointsVictoire(6).setJoueurs(new HashSet<>(List.of(1, 2))).createSommet());
        graphe.ajouterSommet(new Sommet.SommetBuilder().setIndice(3).setSurcout(16).setNbPointsVictoire(8).setJoueurs(new HashSet<>(List.of(0, 2))).createSommet());
        graphe.ajouterSommet(new Sommet.SommetBuilder().setIndice(4).setSurcout(18).setNbPointsVictoire(10).setJoueurs(new HashSet<>(List.of(0, 1))).createSommet());
        Set<Sommet> petitS = new HashSet<>();

        petitS.add(graphe.getSommet(1));
        petitS.add(graphe.getSommet(2));
        petitS.add(graphe.getSommet(3));

        Set<Sommet> s = graphe.getSommets();

        Sommet.SommetBuilder sommetBuilder = new Sommet.SommetBuilder();
        sommetBuilder.setIndice(1);
        sommetBuilder.setSurcout(42);
        sommetBuilder.setNbPointsVictoire(18);
        Set<Integer> joueurs = new HashSet<>();
        sommetBuilder.setJoueurs(new HashSet<>(List.of(0, 1, 2)));

        s.add(sommetBuilder.createSommet());

        Graphe g = new Graphe(s);
        Sommet newSommet = new Sommet(graphe.getSommet(1));
        boolean depasser = false;
        for (Sommet s2 : g.getSommets()){
            if (!depasser && s2.getIndice()==1){
                depasser = true;
            } else if (s2.getIndice()==1) {
                newSommet = s2;
                break;
            }
        }

        assertEquals(g.getSommets() ,Graphe.fusionnerEnsembleSommets(graphe, petitS).getSommets());
        Graphe nouveau = Graphe.fusionnerEnsembleSommets(graphe, petitS);
        assertEquals(sommetBuilder.createSommet().getSurcout(), newSommet.getIndice());
        assertEquals(sommetBuilder.createSommet().getNbPointsVictoire(), nouveau.getSommet(1).getNbPointsVictoire());
        assertEquals(sommetBuilder.createSommet().getJoueurs(), nouveau.getSommet(1).getJoueurs());
    }

    @Test
    public void test_coloration_gloutonne() {  // soucis : genere 10 combinaisons alors que 3 parmi 5 ca fait 20. Il manque la bonne séquence egale au graphe
        Graphe g = new Graphe();

        Sommet.SommetBuilder sommet = new Sommet.SommetBuilder();
        sommet.setIndice(1);
        Sommet s = sommet.createSommet();

        sommet.setIndice(2);
        Sommet s2 = sommet.createSommet();

        sommet.setIndice(3);
        Sommet s3 = sommet.createSommet();

        sommet.setIndice(4);
        Sommet s4 = sommet.createSommet();

        sommet.setIndice(5);
        Sommet s5 = sommet.createSommet();


        g.ajouterSommet(s);
        g.ajouterSommet(s2);
        g.ajouterSommet(s3);
        g.ajouterSommet(s4);
        g.ajouterSommet(s5);

        g.ajouterArete(s, s2);
        g.ajouterArete(s2, s3);
        g.ajouterArete(s3, s4);
        g.ajouterArete(s4, s5);
        g.ajouterArete(s5, s2);
        g.ajouterArete(s, s5);


        Map<Integer, Set<Sommet>> map = g.getColorationGloutonne();

        assertFalse(g.estCycle());
    }

    @Test
    public void test_coloration_gloutonne_indice_de_meme_degre() {  // soucis : genere 10 combinaisons alors que 3 parmi 5 ca fait 20. Il manque la bonne séquence egale au graphe
        Graphe g = new Graphe();

        Sommet.SommetBuilder sommet = new Sommet.SommetBuilder();
        sommet.setIndice(1);
        Sommet s = sommet.createSommet();

        sommet.setIndice(2);
        Sommet s2 = sommet.createSommet();

        sommet.setIndice(3);
        Sommet s3 = sommet.createSommet();

        sommet.setIndice(4);
        Sommet s4 = sommet.createSommet();



        g.ajouterSommet(s);
        g.ajouterSommet(s2);
        g.ajouterSommet(s3);
        g.ajouterSommet(s4);

        g.ajouterArete(s, s2);
        g.ajouterArete(s2, s3);
        g.ajouterArete(s3, s4);
        g.ajouterArete(s,s4);

        Map<Integer, Set<Sommet>> map = g.getColorationGloutonne();
        for (Integer a : map.keySet()) {
            assertTrue(a.equals(1) || a.equals(2));
            assertNotEquals(3, (int) a);
        }
        Set<Sommet> ens = new HashSet<>();
        ens.add(s);
        ens.add(s3);

        Set<Sommet> ens2 = new HashSet<>();
        ens2.add(s2);
        ens2.add(s4);

        assertTrue(map.get(1).containsAll(ens));
        assertTrue(map.get(2).containsAll(ens2));
        assertFalse(map.get(1).containsAll(ens2));
        assertEquals(2, map.size());

        assertFalse(map.isEmpty());
        assertTrue(g.estCycle());
    }
}