import java.io.*;
import java.util.*;
import java.nio.file.*;

public class SimpleFileManager {
    public static void main(String [] argc) {
        System.out.println("Use \"help\" for more information");
        Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        try {
            try {
                Path abs = Paths.get((new File("")).getAbsolutePath());
                while(in.hasNextLine()) {
                    String [] arguments = in.nextLine().split(" ");
                    DirectoryStream<Path> stream = Files.newDirectoryStream(abs);
                    if (arguments.length > 2) {
                        printErrorInfFunc();
                    } else if(arguments[0].equals("exit")) {
                        break;
                    } else if (arguments[0].equals("help")) {
                        printHelpFunc();
                    } else if(arguments[0].equals("dir")) {
                        dirFunc(abs, stream);
                    } else if(arguments.length != 2) {
                        printErrorInfFunc();
                    } else if(arguments[0].equals("rm")) {
                        rmFunc(arguments[1], stream);
                    } else if(arguments[0].equals("cd")) {
                        abs = cdFunc(abs, arguments[1]);
                    } else if(arguments[0].equals("create")) {
                        createFunc(abs, arguments[1]);
                    } else if(arguments[0].equals("mkdir")) {
                        mkdirFunc(abs, arguments[1]);
                    } else if(arguments[0].equals("rmdir")) {
                        rmdirFunc(abs, arguments[1]);
                    } else {
                        printErrorInfFunc();
                    } 
                    System.out.println("------------------------------------------");
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } finally {
            in.close();
        }
    }
    
    private static void printErrorInfFunc() {
        System.out.println("ERROR, please write correct command(use help command for more information)");
    }

    private static void printHelpFunc() {
        System.out.println("-exit: close FileManager");
        System.out.println("-dir: information about current directory");
        System.out.println("-rm <filename>: delete a file");
        System.out.println("-cd <directory>: move to directory");
        System.out.println("-create <filename>: create a file");
        System.out.println("-mkdir <directory>: create a directory");
        System.out.println("-rmdir <directory>: delete a directory");
    }

    private static void deleteFunc(File file) {
        System.out.println("Deleting: " + file.getAbsolutePath());
        if(file.isDirectory()) {
            String [] files = file.list();
            if((files != null) && (files.length != 0)) {
                for(String f: files) {
                    deleteFunc(new File(file.getAbsolutePath() + File.separator + f));
                }
            }
        }
        file.delete();
        System.out.println("Deleted: " + file.getAbsolutePath());
    }

    private static void rmFunc(String name, DirectoryStream<Path> stream) throws IOException {
        System.out.println("Deleting: " + name);
        boolean flag = false;
        for(Path p : stream) {
            if(p.getFileName().toString().equals(name)) {
                flag = Files.deleteIfExists(p);
            }
        }
        if (flag) {
            System.out.println("Deleted: " + name);
        } else {
            System.out.println("Cannot find a file");
        }
    }

    private static void mkdirFunc(Path abs, String name) {
        Path temp = abs.resolve(name).normalize();
        try {
            Files.createDirectory(temp);
            System.out.println("Succesfull");
        } catch (FileAlreadyExistsException e) {
            System.out.println("Directory already exists");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void createFunc(Path abs, String name) {
        Path temp = abs.resolve(name).normalize();
        try {
            Files.createFile(temp);
            System.out.println("Succesfull");
        } catch (FileAlreadyExistsException e) {
            System.out.println("File already exists");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void rmdirFunc(Path abs, String name) {
        Path temp = abs.resolve(name).normalize();
        if (Files.isDirectory(temp)) {
            deleteFunc(temp.toFile());
        } else {
            System.out.println("This is not a directory");
        }
    }

    private static void dirFunc(Path abs, DirectoryStream<Path> stream) {
        for(Path p : stream) {
            System.out.println("\t" + p.getFileName().toString());
        }
        System.out.println("Current directory: " + abs.toString());
    }

    private static Path cdFunc(Path abs, String name) {
        Path temp = abs.resolve(name).normalize();
        if (Files.isDirectory(temp)) {
            abs = temp;
        } else {
            System.out.println("Path does not exist");
        }
        System.out.println("Current directory: " + abs.toString());
        return abs;
    }
}
