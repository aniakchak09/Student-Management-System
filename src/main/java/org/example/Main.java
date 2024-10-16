package org.example;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        String inputPath = "src/main/resources/" + args[0] + "/" + args[0] + ".in";
        String outputPath = "src/main/resources/" + args[0] + "/" + args[0] + ".out";

        Secretariat secretariat = new Secretariat();

        try (BufferedReader br = new BufferedReader(new FileReader(inputPath))) {   //o fi oke sa am atatea lucruri in try?
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(" - ");

                switch (data[0]) {
                    case "adauga_student":
                        Secretariat.adaugaStud(outputPath, data);
                        break;
                    case "citeste_mediile":
                        Secretariat.adaugaMedii("src/main/resources/" + args[0] + "/");
                        break;
                    case "posteaza_mediile":
                        Secretariat.posteazaMedii(outputPath);
                        break;
                    case "contestatie":
                        Secretariat.contestatii(data[1], Double.parseDouble(data[2]));
                        break;
                    case "adauga_curs":
                        Secretariat.adaugaCurs(data);
                        break;
                    case "adauga_preferinte":
                        Secretariat.adaugaPreferinte(data);
                        break;
                    case "repartizeaza":
                        Secretariat.repartizeaza();
                        break;
                    case "posteaza_curs":
                        Secretariat.posteazaCurs(outputPath, data[1]);
                        break;
                    case "posteaza_student":
                        Secretariat.posteazaStudent(outputPath, data[1]);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
