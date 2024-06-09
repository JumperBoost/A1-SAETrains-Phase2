package fr.umontpellier.iut.graphes;


import java.util.*;

/**
 * Graphe simple non-orienté pondéré représentant le plateau du jeu.
 * Pour simplifier, on supposera que le graphe sans sommets est le graphe vide.
 * Le poids de chaque sommet correspond au coût de pose d'un rail sur la tuile correspondante.
 * Les sommets sont indexés par des entiers (pas nécessairement consécutifs).
 */

public class Graphe {
    private final Set<Sommet> sommets;

    public Graphe(Set<Sommet> sommets) {
        this.sommets = sommets;
    }

    /**
     * Construit un graphe à n sommets 0..n-1 sans arêtes
     */
    public Graphe(int n) {
        this();
        for(int nbSommet = 0; nbSommet < n; nbSommet++){
            ajouterSommet(nbSommet);
        }
    }

    /**
     * Construit un graphe vide
     */
    public Graphe() {
        sommets = new HashSet<>();
    }

    /**
     * Construit un sous-graphe induit par un ensemble de sommets
     * sans modifier le graphe donné
     *
     * @param g le graphe à partir duquel on construit le sous-graphe
     * @param X les sommets à considérer (on peut supposer que X est inclus dans l'ensemble des sommets de g,
     *          même si en principe ce n'est pas obligatoire)
     */
    public Graphe(Graphe g, Set<Sommet> X) {
        this();
        for(Sommet sommetGraphe : g.getSommets()) {
            if (X.contains(sommetGraphe)) {
                Sommet sommet = new Sommet(sommetGraphe);
                for(Sommet voisin : sommetGraphe.getVoisins())
                    if(!X.contains(voisin))
                        sommet.supprimerVoisin(voisin);
                ajouterSommet(sommet);
            }
        }
    }

    /**
     * @return true si et seulement si la séquence d'entiers passée en paramètre
     * correspond à un graphe simple valide dont les degrés correspondent aux éléments de la liste.
     * Pré-requis : on peut supposer que la séquence est triée dans l'ordre croissant.
     */
    public static boolean sequenceEstGraphe(List<Integer> sequence) {
        int sommeSequence = 0, degreMax = 0;
        for(int i : sequence) {
            sommeSequence += i;
            if(i > degreMax)
                degreMax = i;
        }

        // Vérification séquence paire et degré max dans l'encadrement 0 < max < n
        if(sommeSequence % 2 == 0 && degreMax > 0 && degreMax < sequence.size()) {
            List<Integer> seqCopy = new ArrayList<>(sequence);
            for(int i = seqCopy.size()-1; i > 0; i--) {
                int nbRestants = seqCopy.get(i);
                int j = i-1;
                while (j >= 0 && nbRestants != 0) {
                    if(seqCopy.get(j) != 0) {
                        seqCopy.set(j, seqCopy.get(j)-1);
                        nbRestants--;
                    }
                    j--;
                }
                if(nbRestants != 0)
                    return false;
            }
        } else return false;
        return true;
    }

    /**
     * @param g        le graphe source, qui ne doit pas être modifié
     * @param ensemble un ensemble de sommets
     *                 pré-requis : l'ensemble donné est inclus dans l'ensemble des sommets de {@code g}
     * @return un nouveau graph obtenu en fusionnant les sommets de l'ensemble donné.
     * On remplacera l'ensemble de sommets par un seul sommet qui aura comme indice
     * le minimum des indices des sommets de l'ensemble. Le surcout du nouveau sommet sera
     * la somme des surcouts des sommets fusionnés. Le nombre de points de victoire du nouveau sommet
     * sera la somme des nombres de points de victoire des sommets fusionnés.
     * L'ensemble de joueurs du nouveau sommet sera l'union des ensembles de joueurs des sommets fusionnés.
     */
    public static Graphe fusionnerEnsembleSommets(Graphe g, Set<Sommet> ensemble) {
        Set<Integer> indices = new HashSet<>();
        int surcout = 0;
        int nbPointVictoire = 0;
        Set<Integer> joueurs = new HashSet<>();
        Graphe newGraphe = new Graphe(new HashSet<>(g.getSommets()));

        Set<Sommet> voisins = new HashSet<>();
        for (Sommet s : ensemble){
            indices.add(s.getIndice());
            surcout += s.getSurcout();
            nbPointVictoire += s.getNbPointsVictoire();
            joueurs.addAll(s.getJoueurs());
            voisins.addAll(s.getVoisins());
            newGraphe.supprimerSommet(s);
        }
        voisins.removeAll(ensemble);
        Sommet.SommetBuilder sommetBuilder = new Sommet.SommetBuilder();
        int indice = indices.stream().min(Integer::compareTo).orElse(0);
        sommetBuilder.setIndice(indice);
        sommetBuilder.setJoueurs(joueurs);
        sommetBuilder.setSurcout(surcout);
        sommetBuilder.setNbPointsVictoire(nbPointVictoire);
        newGraphe.ajouterSommet(sommetBuilder.createSommet());
        Sommet newSommet = newGraphe.getSommet(indice);
        voisins.forEach(v -> {
            v.ajouterVoisin(newSommet);
            newSommet.ajouterVoisin(v);
        });
        return newGraphe;
    }

    /**
     * @param i un entier
     * @return le sommet d'indice {@code i} dans le graphe ou null si le sommet d'indice {@code i} n'existe pas dans this
     */
    public Sommet getSommet(int i) {
        for (Sommet s : sommets) {
            if (s.getIndice() == i) {
                return s;
            }
        }
        return null;
    }

    /**
     * @return l'ensemble des sommets du graphe
     */
    public Set<Sommet> getSommets() {
        return sommets;
    }

    /**
     * @return l'ordre du graphe, c'est-à-dire le nombre de sommets
     */
    public int getNbSommets() {
        return sommets.size();
    }

    /**
     * @return l'ensemble d'arêtes du graphe sous forme d'ensemble de paires de sommets
     */
    public Set<Set<Sommet>> getAretes() {
        Set<Set<Sommet>> aretes = new HashSet<>();
        for(Sommet sommet : sommets)
            for(Sommet voisin : sommet.getVoisins())
                aretes.add(Set.of(sommet, voisin));
        return aretes;
    }

    /**
     * @return le nombre d'arêtes du graphe
     */
    public int getNbAretes() {
        return getAretes().size();
    }

    /**
     * Ajoute un sommet d'indice i au graphe s'il n'est pas déjà présent
     *
     * @param i l'entier correspondant à l'indice du sommet à ajouter dans le graphe
     * @return un booléen retournant {@code true} si le sommet a été ajouté, {@code false} sinon
     */
    public boolean ajouterSommet(int i) {
        return sommets.add(new Sommet.SommetBuilder().setIndice(i).createSommet());
    }

    /**
     * Ajoute un sommet au graphe s'il n'est pas déjà présent
     *
     * @param s le sommet à ajouter
     * @return true si le sommet a été ajouté, false sinon
     */
    public boolean ajouterSommet(Sommet s) {
        return sommets.add(s);
    }

    /**
     * Supprime un sommet et ses voisins au graphe s'il est présent
     *
     * @params s le sommet à supprimer
     * Pré-requis : le sommet {@code s} inclus dans le graphe
     * @return true si le sommet a été supprimé, false sinon
     */
    public boolean supprimerSommet(Sommet s) {
        s.getVoisins().forEach(v -> v.supprimerVoisin(s));
        return sommets.remove(s);
    }

    /**
     * @param s le sommet dont on veut connaître le degré
     *          pré-requis : {@code s} est un sommet de this
     * @return le degré du sommet {@code s}
     */
    public int degre(Sommet s) {
        return s.getVoisins().size();
    }

    /**
     * Récupérer la séquence de degré du graphe
     */
    public List<Integer> getSequenceDegres() {
        List<Integer> sequences = new ArrayList<>();
        for(Sommet s : sommets)
            sequences.add(degre(s));
        sequences.sort(Integer::compareTo);
        return sequences;
    }

    /**
     * @return true si et seulement si this est complet.
     */
    public boolean estComplet() {
        return getNbAretes() == (sommets.size()*(sommets.size()-1)) / 2;
    }

    /**
     * @return true si et seulement si this est une chaîne. On considère que le graphe vide est une chaîne.
     */
    public boolean estChaine() {
        if (getNbAretes() == getNbSommets()-1 || getNbSommets() == 0){
            int nbBout = 0;
            for (Sommet s : sommets){
                if (s.getVoisins().size() == 1)
                    nbBout++;

                if (nbBout > 2 || s.getVoisins().isEmpty() || s.getVoisins().size() > 2)
                    return false;
            }
            return true;
        }
        return false;
    }

    /**
     * @return true si et seulement si this est un cycle. On considère que le graphe vide n'est pas un cycle.
     */
    public boolean estCycle() {
        if(getNbSommets() >= 3 && getNbAretes() > getNbSommets()-1 && estConnexe()) {
            for(Sommet sommet : sommets)
                if(degre(sommet) != 2)
                    return false;
            return true;
        }
        return false;
    }

    /**
     * @return {@code true} si et seulement si this est un arbre.
     * On considère qu'un graphe vide est un arbre.
     */
    public boolean estArbre() {
        return sommets.isEmpty() || estConnexe() && !possedeUnCycle();
    }

    /**
     * @return true si et seulement si this est une forêt. On considère qu'un arbre est une forêt
     * et que le graphe vide est un arbre.
     */
    public boolean estForet() {
        Set<Set<Sommet>> ensembleClasses = getEnsembleClassesConnexite();
        Graphe g;
        for(Set<Sommet> classe : ensembleClasses) {
            g = new Graphe(this, classe);
            if(!g.estArbre())
                return false;
        }
        return true;
    }

    /**
     * @return true si et seulement si this a au moins un cycle. On considère que le graphe vide n'est pas un cycle.
     */
    public boolean possedeUnCycle() {
        if(getNbSommets() >= 3) {
            Set<Sommet> dejaVus = new HashSet<>();
            for (Set<Sommet> ensembleClasse : getEnsembleClassesConnexite()) {
                dejaVus.clear();
                if(voisinPossedeUnCycle(dejaVus, sommets.iterator().next(), null))
                    return true;
            }
        }
        return false;
    }

    private boolean voisinPossedeUnCycle(Set<Sommet> dejaVus, Sommet sommetActuel, Sommet dernierSommet) {
        for (Sommet voisin : sommetActuel.getVoisins()) {
            if (voisin != dernierSommet) {
                if (dejaVus.contains(voisin))
                    return true;
                dejaVus.add(voisin);
                if(voisinPossedeUnCycle(dejaVus, voisin, sommetActuel))
                    return true;
            }
        }
        return false;
    }

    /**
     * @return true si et seulement si this a un isthme
     */
    public boolean possedeUnIsthme() {
        if(degreMin() > 1) {
            Graphe graphe = new Graphe(this, sommets);
            Set<Set<Sommet>> aretes = new HashSet<>(graphe.getAretes());
            Set<Set<Sommet>> ensembleClasses;
            Iterator<Set<Sommet>> it = aretes.iterator();
            List<Sommet> arete;
            while (it.hasNext()) {
                ensembleClasses = getEnsembleClassesConnexite();
                arete = it.next().stream().toList();
                graphe.supprimerArete(arete.get(0), arete.get(1));
                if(!getEnsembleClassesConnexite().equals(ensembleClasses))
                    return true;
                graphe.ajouterArete(arete.get(0), arete.get(1));
            }
        } else return true;
        return false;
    }

    public void ajouterArete(Sommet s, Sommet t) {
        if (!s.estVoisin(t)){
            s.ajouterVoisin(t);
            t.ajouterVoisin(s);
        }
    }

    public void supprimerArete(Sommet s, Sommet t) {
        if (s.estVoisin(t)){
            s.supprimerVoisin(t);
            t.supprimerVoisin(s);
        }
    }

    /**
     * @return une coloration gloutonne du graphe sous forme d'une Map d'ensemble indépendants de sommets.
     * L'ordre de coloration des sommets est suivant l'ordre décroissant des degrés des sommets
     * (si deux sommets ont le même degré, alors on les ordonne par indice croissant).
     */
    public Map<Integer, Set<Sommet>> getColorationGloutonne() {
        Map<Integer, Set<Sommet>> colorations = new HashMap<>();
        if(getNbSommets() > 0) {
            int nbCouleurs = degreMax() + 1;
            // Ajouter toutes les couleurs possibles
            for (int i = 1; i <= nbCouleurs; i++)
                colorations.put(i, new HashSet<>());
            PriorityQueue<Sommet> queue = new PriorityQueue<>((a, b) -> degre(a) == degre(b) ? a.getIndice() - b.getIndice() : degre(b) - degre(a));
            queue.addAll(sommets);

            Sommet sommet;
            while (!queue.isEmpty()) {
                sommet = queue.poll();
                int i = 1;
                Set<Sommet> voisins = sommet.getVoisins();
                do {
                    if (colorations.get(i).stream().noneMatch(voisins::contains))
                        colorations.get(i).add(sommet);
                } while (!colorations.get(i++).contains(sommet));
            }
            // Supprimer toutes les couleurs inutilisées
            colorations.entrySet().removeIf(e -> e.getValue().isEmpty());
        }
        return colorations;
    }

    /**
     * @param depart  - ensemble non-vide de sommets
     * @param arrivee
     * @return le surcout total minimal du parcours entre l'ensemble de depart et le sommet d'arrivée
     * pré-requis : l'ensemble de départ et le sommet d'arrivée sont inclus dans l'ensemble des sommets de this
     */
    public int getDistance(Set<Sommet> depart, Sommet arrivee) {
        int plusPetit = Integer.MAX_VALUE;
        for(Sommet s : depart)
            plusPetit = Math.min(plusPetit, getDistance(s, arrivee));
        return plusPetit;
    }

    /**
     * @return le surcout total minimal du parcours entre le sommet de depart et le sommet d'arrivée
     */
    public int getDistance(Sommet depart, Sommet arrivee) {
        Map<Sommet, Integer> distances = new HashMap<>();
        for(Sommet sommet : sommets)
            distances.put(sommet, Integer.MAX_VALUE);
        distances.replace(depart, 0);
        PriorityQueue<Sommet> queue = new PriorityQueue<>((a, b) -> distances.get(a).equals(distances.get(b))
                ? a.getIndice() - b.getIndice() : distances.get(a) - distances.get(b));
        queue.add(depart);
        while (!queue.isEmpty()) {
            Sommet sommet = queue.poll();
            int distance = distances.remove(sommet);
            if(sommet == arrivee)
                return distance;
            for(Sommet voisin : sommet.getVoisins()) {
                if(distances.containsKey(voisin)) {
                    if(!queue.contains(voisin))
                        queue.add(voisin);
                    int nvDistance = distance + voisin.getSurcout();
                    if(nvDistance < distances.get(voisin))
                        distances.replace(voisin, nvDistance);
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    /**
     * @return l'ensemble des classes de connexité du graphe sous forme d'un ensemble d'ensembles de sommets.
     */
    public Set<Set<Sommet>> getEnsembleClassesConnexite() {
        Set<Set<Sommet>> ensembleClassesConnexite = new HashSet<>();
        if (sommets.isEmpty())
            return ensembleClassesConnexite;
        Set<Sommet> sommets = new HashSet<>(this.sommets);
        while (!sommets.isEmpty()) {
            Sommet v = sommets.iterator().next();
            Set<Sommet> classe = getClasseConnexite(v);
            sommets.removeAll(classe);
            ensembleClassesConnexite.add(classe);
        }
        return ensembleClassesConnexite;
    }

    /**
     * @param v un sommet du graphe this
     * @return la classe de connexité du sommet {@code v} sous forme d'un ensemble de sommets.
     */
    public Set<Sommet> getClasseConnexite(Sommet v) {
        if (!sommets.contains(v))
            return new HashSet<>();
        Set<Sommet> classe = new HashSet<>();
        calculerClasseConnexite(v, classe);
        return classe;
    }

    private void calculerClasseConnexite(Sommet v, Set<Sommet> dejaVus) {
        dejaVus.add(v);
        Set<Sommet> voisins = v.getVoisins();

        for (Sommet voisin : voisins) {
            if (dejaVus.add(voisin))
                calculerClasseConnexite(voisin, dejaVus);
        }
    }

    /**
     * @return true si et seulement si this est connexe.
     */
    public boolean estConnexe() {
        if(getNbSommets() > 1) {
            List<Sommet> dejaVus = new ArrayList<>();
            Sommet s = getSommets().iterator().next();
            dejaVus.add(s);
            Set<Sommet> voisins;
            int indice = 0;
            while (dejaVus.size() != sommets.size()) {
                for (Sommet voisin : s.getVoisins())
                    if (!dejaVus.contains(voisin))
                        dejaVus.add(voisin);

                indice++;
                if (indice < dejaVus.size())
                    s = dejaVus.get(indice);
                else return false;
            }
        }
        return true;
    }

    /**
     * @return le degré maximum des sommets du graphe
     */
    public int degreMax() {
        int nbDegreMax = 0;
        for (Sommet s : sommets)
            if (degre(s)>nbDegreMax)
                nbDegreMax = degre(s);
        return nbDegreMax;
    }

    /**
     * @return le degré minimum des sommets du graphe
     */
    public int degreMin() {
        int nbDegreMin = Integer.MAX_VALUE;
        for (Sommet s : sommets)
            if (degre(s)<nbDegreMin)
                nbDegreMin = degre(s);
        return nbDegreMin;
    }

    /**
     * @return une coloration propre optimale du graphe sous forme d'une Map d'ensemble indépendants de sommets.
     * Chaque classe de couleur est représentée par un entier (la clé de la Map).
     * Pré-requis : le graphe est issu du plateau du jeu Train (entre autres, il est planaire).
     */
    public Map<Integer, Set<Sommet>> getColorationPropreOptimale() {
        throw new RuntimeException("Méthode à implémenter");
    }

    /**
     * @return true si et seulement si this possède un sous-graphe complet d'ordre {@code k}
     */
    public boolean possedeSousGrapheComplet(int k) {
        throw new RuntimeException("Méthode à implémenter");
    }

    /**
     * @param g un graphe
     * @return true si et seulement si this possède un sous-graphe isomorphe à {@code g}
     */
    public boolean possedeSousGrapheIsomorphe(Graphe g) {
        throw new RuntimeException("Méthode à implémenter");
    }

    /**
     * @param s
     * @param t
     * @return un ensemble de sommets qui forme un ensemble critique de plus petite taille entre {@code s} et {@code t}
     */
    public Set<Sommet> getEnsembleCritique(Sommet s, Sommet t){
        throw new RuntimeException("Méthode à implémenter");
    }
}
