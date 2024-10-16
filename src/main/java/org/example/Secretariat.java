package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

public class Secretariat {
    public static ArrayList<Licenta> studentiLicenta;
    public static ArrayList<Master> studentiMaster;
    public static ArrayList<Curs<Licenta>> cursuriLicenta;
    public static ArrayList<Curs<Master>> cursuriMaster;

    public Secretariat() {
        studentiLicenta = new ArrayList<>();
        studentiMaster = new ArrayList<>();
        cursuriLicenta = new ArrayList<>();
        cursuriMaster = new ArrayList<>();
    }

    public static void adaugaStud(String outputPath, String[] data) {
        try {
            if (data[1].equals("licenta")) {
                adaugaStudLicenta(data[2]);
            } else if (data[1].equals("master")) {
                adaugaStudMaster(data[2]);
            }
        } catch (Exception err) {
            try (FileWriter fw = new FileWriter(outputPath, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {

                out.println("***\n" + "Student duplicat: " + data[2]);

            } catch (Exception e) {
                e.printStackTrace();
            }
            err.printStackTrace();
        }
    }

    public static void adaugaStudLicenta(String nume) throws DuplicateStudentException {
        for (Licenta student : studentiLicenta) {
            if (student.nume.equals(nume)) {
                throw new DuplicateStudentException("***\n" + "Student duplicat: " + nume);
            }
        }

        for (Master student : studentiMaster) {
            if (student.nume.equals(nume)) {
                throw new DuplicateStudentException("***\n" + "Student duplicat: " + nume);
            }
        }

        Licenta licenta = new Licenta(nume);
        studentiLicenta.add(licenta);
    }

    public static void adaugaStudMaster(String nume) throws DuplicateStudentException {
        for (Master student : studentiMaster) {
            if (student.nume.equals(nume)) {
                throw new DuplicateStudentException("***\n" + "Student duplicat: " + nume);
            }
        }

        for (Licenta student : studentiLicenta) {
            if (student.nume.equals(nume)) {
                throw new DuplicateStudentException("***\n" + "Student duplicat: " + nume);
            }
        }

        Master master = new Master(nume);
        studentiMaster.add(master);
    }

    public static void adaugaMedii(String path) {
        for (int i = 1; ; i++) {
            File f = new File(path + "note_" + i + ".txt");

            if (!f.exists()) {
                break;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(path + "note_" + i + ".txt"))) {
                String line;

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(" - ");

                    for (Licenta student : Secretariat.studentiLicenta) {
                        if (student.nume.equals(data[0])) {
                            student.setMedie(Double.parseDouble(data[1]));
                        }
                    }

                    for (Master student : Secretariat.studentiMaster) {
                        if (student.nume.equals(data[0])) {
                            student.setMedie(Double.parseDouble(data[1]));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Exceptie: " + e.getMessage());
            }
        }
    }

    public static void adaugaCurs(String[] data) {
        if (data[1].equals("licenta")) {
            adaugaCursLicenta(data[2], Integer.parseInt(data[3]));
        } else if (data[1].equals("master")) {
            adaugaCursMaster(data[2], Integer.parseInt(data[3]));
        }
    }

    public static void adaugaCursLicenta(String nume, int capacitate) {
        Curs<Licenta> curs = new Curs<>(nume, capacitate);
        cursuriLicenta.add(curs);
    }

    public static void adaugaCursMaster(String nume, int capacitate) {
        Curs<Master> curs = new Curs<>(nume, capacitate);
        cursuriMaster.add(curs);
    }

    public static void posteazaMedii(String fileName) {
        ArrayList<Student> studenti = new ArrayList<>();
        studenti.addAll(studentiLicenta);
        studenti.addAll(studentiMaster);

        Comparator<Student> comparator = Comparator
                .comparing(Student::getMedie).reversed()
                .thenComparing(Student::getNume);

        studenti.sort(comparator);

        try (FileWriter fw = new FileWriter(fileName, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println("***");

            for (Student student : studenti) {
                out.println(student.nume + " - " + student.medie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void contestatii(String name, double newGrade) {
        for (Licenta student : Secretariat.studentiLicenta) {
            if (student.nume.equals(name)) {
                student.setMedie(newGrade);
            }
        }

        for (Master student : Secretariat.studentiMaster) {
            if (student.nume.equals(name)) {
                student.setMedie(newGrade);
            }
        }
    }

    public static void adaugaPreferinte(String[] data) {
        for (Student student : studentiLicenta) {
            if (student.nume.equals(data[1])) {
                for (String curs : data) {
                    for (Curs<Licenta> cursLicenta : cursuriLicenta) {
                        if (cursLicenta.nume.equals(curs)) {
                            student.preferinte.add(cursLicenta);
                        }
                    }
                }
            }
        }

        for (Student student : studentiMaster) {
            if (student.nume.equals(data[1])) {
                for (String curs : data) {
                    for (Curs<Master> cursMaster : cursuriMaster) {
                        if (cursMaster.nume.equals(curs)) {
                            student.preferinte.add(cursMaster);
                        }
                    }
                }
            }
        }
    }

    public static void repartizeaza() { //we will asume this is correct
        Comparator<Student> comparator = Comparator
                .comparing(Student::getMedie).reversed()
                .thenComparing(Student::getNume);

        studentiLicenta.sort(comparator);

        for (Licenta student : studentiLicenta) {
            for (Curs<Student> curs : student.preferinte) {
                if (verificaPreferinta(comparator, student, curs)) break;
            }
        }

        for (Licenta student : studentiLicenta) {
            if (!student.assigned) {
                for (Curs<Licenta> curs : cursuriLicenta) {
                    if (redistribuie(comparator, student, curs)) break;
                }
            }
        }

        studentiMaster.sort(comparator);

        for (Master student : studentiMaster) {
            for (Curs<Student> curs : student.preferinte) {
                if (verificaPreferinta(comparator, student, curs)) break;
            }
        }

        for (Master student : studentiMaster) {
            if (!student.assigned) {
                for (Curs<Master> curs : cursuriMaster) {
                    if (redistribuie(comparator, student, curs)) break;
                }
            }
        }
    }

    private static boolean verificaPreferinta(Comparator<Student> comparator, Student student, Curs<Student> curs) {
        if (curs.studenti.size() < curs.capacitate) {
            curs.studenti.add(student);
            curs.studenti.sort(comparator);

            student.preferinte.clear(); //ne imaginam ca aici salvam cursul la care a intrat
            student.preferinte.add(curs);
            student.assigned = true;

            return true;
        } else if (curs.studenti.get(curs.studenti.size() - 1).medie <= student.medie) {
            curs.studenti.add(student);

            student.preferinte.clear();
            student.preferinte.add(curs);
            student.assigned = true;

            return true;
        }
        return false;
    }

    private static boolean redistribuie(Comparator<Student> comparator, Student student, Curs curs) {
        if (curs.studenti.size() < curs.capacitate) {   //il adauga la primul curs cu locuri libere
            curs.studenti.add(student); //indiferent de medie, deoarece nu mai exista preferinte acum
            curs.studenti.sort(comparator);

            student.preferinte.clear();
            student.preferinte.add(curs);
            student.assigned = true;

            return true;
        }
        return false;
    }

    public static void posteazaCurs(String fileName, String curs) {
        for (Curs<Licenta> cursLicenta : cursuriLicenta) {
            if (cursLicenta.nume.equals(curs)) {
                try (FileWriter fw = new FileWriter(fileName, true);
                     BufferedWriter bw = new BufferedWriter(fw);
                     PrintWriter out = new PrintWriter(bw)) {
                    out.println("***");
                    out.println(cursLicenta.nume + " (" + cursLicenta.capacitate + ")");

                    Comparator<Student> comparator = Comparator
                            .comparing(Student::getNume);

                    cursLicenta.studenti.sort(comparator);

                    for (Licenta student : cursLicenta.studenti) {
                        out.println(student.nume + " - " + student.medie);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        for (Curs<Master> cursMaster : cursuriMaster) {
            if (cursMaster.nume.equals(curs)) {
                try (FileWriter fw = new FileWriter(fileName, true);
                     BufferedWriter bw = new BufferedWriter(fw);
                     PrintWriter out = new PrintWriter(bw)) {
                    out.println("***");
                    out.println(cursMaster.nume + " (" + cursMaster.capacitate + ")");

                    Comparator<Student> comparator = Comparator
                            .comparing(Student::getNume);

                    cursMaster.studenti.sort(comparator);

                    for (Master student : cursMaster.studenti) {
                        out.println(student.nume + " - " + student.medie);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void posteazaStudent(String fileName, String nume) {
        ArrayList<Student> studenti = new ArrayList<>();
        studenti.addAll(studentiLicenta);
        studenti.addAll(studentiMaster);

        Comparator<Student> comparator = Comparator
                .comparing(Student::getNume);

        studenti.sort(comparator);

        try (FileWriter fw = new FileWriter(fileName, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            for (Student student : studenti) {
                if (student.nume.equals(nume)) {
                    out.println("***");
                    out.print("Student ");

                    if (student instanceof Licenta)
                        out.print("Licenta: ");
                    else if (student instanceof Master)
                        out.print("Master: ");

                    out.print(student.nume + " - " + student.medie + " - " + student.preferinte.get(0).getNume() + "\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}