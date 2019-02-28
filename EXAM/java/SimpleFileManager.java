import java.io.*;
import java.util.*;
import java.nio.file.*;

public class SimpleFileManager {
    public static void main(String [] args) {
        System.out.println("Use \"help\" for more information");
        Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        try {
            try {
                Path abs = Paths.get((new File("")).getAbsolutePath());
                loop:
                while(in.hasNextLine()) {
                    String [] arguments = in.nextLine().split(" ");
                    if (arguments.length == 0) {
                        continue loop;
                    } else if (arguments.length > 2) {
                        printErrorInfFunc();
                        continue loop;
                    } 
                    try (DirectoryStream<Path> stream = Files.newDirectoryStream(abs)) {
                        String func = arguments[0];
                        switch (func) {
                            case "exit":   
                                break loop;
                            case "help":   
                                printHelpFunc();
                                continue loop;
                            case "dir":
                                dirFunc(abs, stream);
                                continue loop;
                            default:
                                if (arguments.length != 2) {
                                    printErrorInfFunc();
                                    continue loop;
                                } 
                                break;
                        }
                        String arg = arguments[1];
                        switch (func) {
                            case "rm":   
                                rmFunc(arg, stream);
                                continue loop;
                            case "cd":
                                abs = cdFunc(abs, arg);
                                continue loop;
                            case "touch":
                                touchFunc(abs, arg);
                                continue loop;
                            case "mkdir":
                                mkdirFunc(abs, arg);
                                continue loop;
                            case "rmdir":
                                mkdirFunc(abs, arg);
                                continue loop;
                            default:
                                printErrorInfFunc();
                                break;
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } finally {
            in.close();
        }
    }
    
    private static void printErrorInfFunc() {
        System.out.println("ERROR, please write correct command");
        printHelpFunc();
    }

    private static void printHelpFunc() {
        System.out.println("exit: close FileManager");
        System.out.println("dir: information about current directory");
        System.out.println("rm <filename>: delete a file");
        System.out.println("cd <directory>: move to directory");
        System.out.println("create <filename>: create a file");
        System.out.println("mkdir <directory>: create a directory");
        System.out.println("rmdir <directory>: delete a directory");
    }

    private static void deleteFunc(File file) {
        System.out.println("Deleting: " + file.getAbsolutePath());
        if (file.isDirectory()) {
            String [] files = file.list();
            if (files != null && files.length != 0) {
                for (String f: files) {
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
        for (Path p : stream) {
            if (p.getFileName().toString().equals(name)) {
                flag = Files.deleteIfExists(p);
            }
        }
        System.out.println((flag ? "Deleted: " : "Cannot find a file ") + name);
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

    private static void touchFunc(Path abs, String name) {
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
        for (Path p : stream) {
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
