package com.example.gymtracker.Model;

public class TestDatenForTestingOnly {

    public static User getTestUser(){
        User testUser = new User("Julius@Test", "12345678");
        testUser.addTraining(new Training("BauchBeineBrust") );
        testUser.addTraining(new Training("Brust Beine Biegen"));

        testUser.getTrainingsOfUser().get(0).addUebung(new Uebung("Bauchprese", 5) );
        testUser.getTrainingsOfUser().get(0).addUebung(new Uebung("Beinstrecken", 3) );
        testUser.getTrainingsOfUser().get(0).addUebung(new Uebung("Brust strecken", 4) );
        testUser.getTrainingsOfUser().get(1).addUebung(new Uebung("Bankdruecken", 5));
        testUser.getTrainingsOfUser().get(1).addUebung(new Uebung("Kniebeugen", 2));
        testUser.getTrainingsOfUser().get(1).addUebung(new Uebung("Biegen", 2));

        testUser.getTrainingsOfUser().get(1).getUebungListe().get(0).Saetze.add(new Satz(10,80));
        testUser.getTrainingsOfUser().get(1).getUebungListe().get(0).Saetze.add(new Satz(10,80));
        testUser.getTrainingsOfUser().get(1).getUebungListe().get(0).Saetze.add(new Satz(9,80));
        testUser.getTrainingsOfUser().get(1).getUebungListe().get(0).Saetze.add(new Satz(9,80));
        testUser.getTrainingsOfUser().get(1).getUebungListe().get(0).Saetze.add(new Satz(8,80));

        testUser.getTrainingsOfUser().get(1).getUebungListe().get(1).Saetze.add(new Satz(12,75));
        testUser.getTrainingsOfUser().get(1).getUebungListe().get(1).Saetze.add(new Satz(11,75));
        testUser.getTrainingsOfUser().get(1).getUebungListe().get(1).Saetze.add(new Satz(10,75));
        testUser.getTrainingsOfUser().get(1).getUebungListe().get(1).Saetze.add(new Satz(10,75));
        testUser.getTrainingsOfUser().get(1).getUebungListe().get(1).Saetze.add(new Satz(6,75));

        return testUser;
    }


}
