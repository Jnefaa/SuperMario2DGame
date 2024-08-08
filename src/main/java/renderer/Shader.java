package renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Shader {
    private int shaderProgramID ;
    private String vertexSource ;
    private String filepath ;
    private String fragmentSource;


    public Shader(String filepath) {
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");
            //find the first pattern after #type " patern "
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n",index);
            String firstPattern = source.substring(index,eol).trim();
            index = source.indexOf("#type",eol) + 6 ;
            eol =source.indexOf("\r\n",index);
            String secondPattern = source.substring(index,eol).trim();

            if (firstPattern.equals("vertex")) {
                vertexSource = splitString[1];
            } else if (firstPattern.equals("fragment")) {
                    fragmentSource = splitString[1] ;

            }
            else {
                throw new IOException("unexpected token " + firstPattern + "in " );
            }
            //Second Pattern
            if (secondPattern.equals("vertex")) {
                vertexSource = splitString[1];
            } else if (secondPattern.equals("fragment")) {
                fragmentSource = splitString[1] ;

            }
            else {
                throw new IOException("unexpected token " + secondPattern + "in " );
            }

        }
        catch(Exception e) {
            e.printStackTrace();
            assert false : "Shader creation failed " + filepath + "' ";
        }
        System.out.println(vertexSource);
        System.out.println(fragmentSource);
    }
    public void compile(){

    }
    public void use() {

    }
    public void detach() {


    }
}
