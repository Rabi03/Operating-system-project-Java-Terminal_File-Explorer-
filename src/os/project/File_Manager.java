package os.project;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class File_Manager{
    public static Stack<String> stack = new Stack<String>();
    public static String str="start";
    public static Stack<String> copy=new Stack<String>() ;
    public static void main(String[] args) throws IOException {
        System.out.println("Enter 'help'  to see available commands");
        Scanner sc=new Scanner(System.in);
//
        stack.push("FileExplorer\\");

        while(!str.endsWith("exit")) {
            System.out.print("$ "+stack.peek()+" > ");
            str=sc.nextLine();

                if(str.startsWith("This Pc")) {
                    File[] drives = File.listRoots();
                    if (drives != null && drives.length > 0) {
                        for (File aDrive : drives) {
                            System.out.println("Local Volume : (" + aDrive.toString() + ") : " + aDrive.getFreeSpace() / 1000000000 + " GB free of " + aDrive.getTotalSpace() / 1000000000 + " GB");
                        }
                    }
                    System.out.print("Select a driver: (C/D/E) : ");
                    str = sc.nextLine();
                    stack.push(str + ":\\");
                    File dir = new File(str + ":\\");
                    File[] files = dir.listFiles();
                    if (files.length == 0) {
                        System.out.println("The directory is empty");
                    } else {
                        String form = " %1$-40s %2$-20s %3$-40s \n";
                        System.out.format(form,"Name" ,"Type" ,"Size");
                        System.out.format(form,"-----" ,"-----" ,"----");
                        for (File aFile : files) {
                            if (!aFile.isHidden()) {
                                if (aFile.isDirectory()) {
                                    String format = " %1$-40s %2$-20s %3$-40s \n";
                                    System.out.format(format,aFile.getName() ,"directory" ,aFile.length()+" KB");
                                } else{
                                    String format = " %1$-40s %2$-20s %3$-40s \n";
                                    System.out.format(format,aFile.getName() ,"file" ,aFile.length()+" KB");
                                }
                            }
                        }
                    }
                }
                else if(str.startsWith("Select")){
                    String[] curr_path=str.split("Select ");
                    String path=stack.peek()+curr_path[1]+"\\";
                    File dir = new File(path);
                    if(dir.exists())
                    stack.push(stack.peek()+curr_path[1]+"\\");
                    else
                        System.out.println(curr_path[1]+" is not exits");
                }
                else if(str.startsWith("Back")){
                    stack.pop();
                }

                else if(str.startsWith("dir")){
                    String dirPath = stack.peek();
                    File dir = new File(dirPath);

                    if(dir.exists()) {
                        File[] files = dir.listFiles();
                        if (files.length == 0) {
                            System.out.println("The directory is empty");
                        } else {
                            String form = " %1$-40s %2$-20s %3$-40s \n";
                            System.out.format(form,"Name" ,"Type" ,"Size");
                            System.out.format(form,"-----" ,"-----" ,"----");
                            for (File aFile : files) {
                                if (!aFile.isHidden()) {
                                    if (aFile.isDirectory()) {
                                        String format = " %1$-40s %2$-20s %3$-40s \n";
                                        System.out.format(format,aFile.getName() ,"directory" ,aFile.length()+" KB");
                                    }
                                }
                            }
                        }
                    }

                }
                else if(str.startsWith("file")){
                    String dirPath = stack.peek();
                    File dir = new File(dirPath);

                    if(dir.exists()) {
                        File[] files = dir.listFiles();
                        if (files.length == 0) {
                            System.out.println("The directory is empty");
                        } else {
                            String form = " %1$-40s %2$-20s %3$-40s \n";
                            System.out.format(form,"Name" ,"Type" ,"Size");
                            System.out.format(form,"-----" ,"-----" ,"----");
                            for (File aFile : files) {
                                if (!aFile.isHidden()) {
                                    if (aFile.isFile()) {
                                        String format = " %1$-40s %2$-20s %3$-40s \n";
                                        System.out.format(format,aFile.getName() ,"file" ,aFile.length()+" KB");
                                    }
                                }
                            }
                        }
                    }

                }
                else if(str.startsWith("mkdir")){
                    String[] curr_path=str.split("mkdir ");
                    String path=stack.peek()+curr_path[1]+"\\";
                    File f=new File(path);
                    if(f.exists()) {
                        System.out.println("'"+curr_path[1]+"'"+" is already exists. Please create a unique one.");
                    }
                    else{
                        f.mkdir();
                        }
                }
                else if(str.startsWith("mkfile")){
                    String[] curr_path=str.split("mkfile ");

                    String path=stack.peek()+curr_path[1];
                    String repath=path.replace("\\", "\\\\");

                    File f=new File(repath);
                    System.out.println(repath);
                    if(!f.exists()){
                        f.createNewFile();
                    }
                    else System.out.println("File is already exists.Please provide a unique one");

                }
                else if(str.equals("")){
                    continue;
                }

                else if(str.startsWith("Copy")){
                    String[] curr_path=str.split("Copy ");

                    if(curr_path[1].equals("dir")){
                        File dir=new File(stack.peek());
                        System.out.println(dir.getName());
                        copy.push(dir.getName());
                        copy.push(stack.peek().replace("\\", "\\\\"));
                        copy.push("Copy");
                    }
                    else {
                        String path = stack.peek() + curr_path[1];

                        copy.push(curr_path[1]);

                        copy.push(path.replace("\\", "\\\\"));
                        copy.push("Copy");
                    }

                }


                else if(str.startsWith("Paste")){
                    String type=copy.pop();
                    File filepath=new File(copy.pop());
                    String fileName = copy.pop();
                    String dstPath = stack.peek() + fileName;
                    if(filepath.isDirectory()){
                        copyDirectory(filepath,new File(dstPath));
                    }
                    else {
                        File dst = new File(dstPath.replace("\\", "\\\\"));


                        Files.copy(filepath.toPath(), dst.toPath());
                    }
                    if(type.equals("Cut"))delete(filepath.toPath());

                }

                else if(str.startsWith("Rename")){
                    String[] curr_path=str.split(" ");
                    System.out.println(curr_path[1]+" "+curr_path[2]);
                    String path1=stack.peek()+curr_path[1];
                    File f1=new File(path1);
                    if(f1.exists()){
                        Files.move(f1.toPath(),f1.toPath().resolveSibling(curr_path[2]));
                    }
                }

                else if(str.startsWith("Delete")){
                    String[] curr_path=str.split("Delete ");
                    String path1=stack.peek()+curr_path[1];
                    File f1=new File(path1);
                    if(f1.exists()){
                        delete(f1.toPath());
                    }
                }

                else if(str.startsWith("Open")){
                    String[] curr_path=str.split("Open ");
                    String path1=stack.peek()+curr_path[1];
                    File f1=new File(path1);
                    Desktop desktop = Desktop.getDesktop();
                    if(!f1.exists()) System.out.println("File doesn't exist");
                    if(f1.isDirectory()){
                        System.out.println("To Open directory use 'Select' command");
                    }
                    else{
                        if(!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not
                        {
                            System.out.println("not supported");
                        }
                        else{
                            desktop.open(f1);
                        }
                    }
                }

                else if(str.startsWith("Volume")){
                    String[] curr_path=str.split("Volume ");
                    stack.push(curr_path[1]+":\\");
                }

                else if(str.startsWith("Cut")){
                    String[] curr_path=str.split("Cut ");

                    if(curr_path[1].equals("dir")){
                        File dir=new File(stack.peek());
                        System.out.println(dir.getName());
                        copy.push(dir.getName());
                        copy.push(stack.peek().replace("\\", "\\\\"));
                        copy.push("Cut");
                    }
                    else {
                        String path = stack.peek() + curr_path[1];

                        copy.push(curr_path[1]);

                        copy.push(path.replace("\\", "\\\\"));
                        copy.push("Cut");
                    }
                }


                else if(str.equals("Help")){
                    System.out.println("**************************************"
                            + "\nThis Pc  : Show available volumes "
                            + "\nSelect  : Change  directory/folder "
                            + "\nfile       : Show content of folder"
                            + "\ndir   : show directories or folders"
                            + "\nBack   : previous Directory"
                            + "\nmkdir  : Create Directory"
                            + "\nmkfile : Create File"
                            + "\nDelete   : Delete File or Folder"
                            + "\nCopy/Paste   : Copy or paste File or Folder"
                            + "\nexit     : Exit"
                            + "\n**************************************");
                }

                else {
                    System.out.println("Please Enter a valid Command");
                    continue;
                }
            }
        }

    private static void copyDirectory(File sourceDir, File destDir) throws IOException {
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        for (String f : sourceDir.list()) {
            File source = new File(sourceDir, f);
            File destination = new File(destDir, f);

            if (source.isDirectory()) {
                copyDirectory(source, destination);
            }
            else {
                copyFile(source, destination);
            }
        }
    }

    private static void copyFile(File sourceFile, File destinationFile) throws IOException {
        FileInputStream input = new FileInputStream(sourceFile);
        FileOutputStream output = new FileOutputStream(destinationFile);

        byte[] buf = new byte[1024];
        int bytesRead;

        while ((bytesRead = input.read(buf)) > 0) {
            output.write(buf, 0, bytesRead);
        }

        input.close();
        output.close();
    }

    private static void delete(Path p) throws IOException {
        if (!p.toFile().exists()) {
            return;
        } else if(p.toFile().isFile()){
            Files.delete(p);
        } else if(p.toFile().isDirectory()){

            try (DirectoryStream<Path> ds = Files.newDirectoryStream(p)) {
                for (Path subPath : ds){
                    delete(subPath);
                }
            }

            Files.delete(p);
        }
    }
    }

