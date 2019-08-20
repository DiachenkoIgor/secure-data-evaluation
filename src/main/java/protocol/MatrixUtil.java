package protocol;

public class MatrixUtil {
    public static int[][] fillMatrix(int a,int b){
        int[][] matrix=new int[a][b];

        for(int i=0;i<matrix.length;i++){
            for(int j=0;j<matrix[i].length;j++){
                matrix[i][j]=(int)(Math.random()*100);
            }
        }
        return matrix;
    }
    public static void checkArray(int[] a){
        a[1]=100;
    }
    public static void checkVariable(int a){
        a++;
    }
    public static void printMatrix(int[][] matrix){
        for(int i=0;i<matrix.length;i++){
            for(int j=0;j<matrix[i].length;j++){
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println();
        }
    }
}
