	import java.util.Scanner;
	import java.io.*;
	import java.io.FileReader;	
	class ass1{

		public static void main(String[] args) throws Exception{
			String fileIn = InputFile();

			double[] weights = read1stlineweights(fileIn);

			String[] columns = read1stlinecolumns(fileIn);

			int Num_of_Parameter = weights.length;
			
			Student[] students = readstudents(fileIn, weights, Num_of_Parameter);	

			double[] averages = calculate_avg(students, Num_of_Parameter);

			print_result(columns, students, averages);	
		}

		public static double[] read1stlineweights(String filename) throws Exception{
			double[] weights_local = new double[1];
			double[] temp;
			BufferedReader reader = new BufferedReader(new FileReader(filename));
            		String line = reader.readLine();
            		if(line != null){
				int lastlocation = 29;
				int i;
				//Record the last location of the iterator in the 1st line, skipping the stuID and stuName

				for(i = 0; lastlocation != line.lastIndexOf(',',line.lastIndexOf(',')-1)+1; i++){
					temp = new double[weights_local.length];
					
					for(int j = 0; j < weights_local.length; j++)
						temp[j] = weights_local[j];

					weights_local = new double[i+1];

					for(int j = 0; j < temp.length; j++)
						weights_local[j] = temp[j];

					weights_local[i] = Double.valueOf(line.substring(line.indexOf(',',lastlocation)+1,line.indexOf(',',line.indexOf(',',lastlocation)+1)));
					//Record the ith weight for every pair of grading, e.g: Case, 10,

					lastlocation = line.indexOf(',',line.indexOf(',',lastlocation)+1)+1;
					//Update the lastlocation like this: Case, 10,
					//					     ^
				}
				temp = new double[weights_local.length];

				for(int j = 0; j < weights_local.length; j++)
					temp[j] = weights_local[j];

				weights_local = new double[i+1];

				for(int j = 0; j < temp.length; j++)
					weights_local[j] = temp[j];
			
				weights_local[i] = Double.valueOf(line.substring(line.indexOf(',',lastlocation)+1,line.length()));
				return weights_local;	
               		}
			else{
				System.out.println("No content in the chosen file!");
				return weights_local;
			}
		}

		public static String[] read1stlinecolumns(String filename) throws Exception{
			String[] columns_local = new String[1];
			String[] temp;
			BufferedReader reader = new BufferedReader(new FileReader(filename));
            		String line = reader.readLine();
            		if(line != null){
				int lastlocation = 20;
				int i;
				//Record the last location of the iterator in the 1st line, skipping the stuID and stuName

				for(i = 0; lastlocation != line.lastIndexOf(',')+1; i++){
					temp = new String[columns_local.length];
					
					for(int j = 0; j < columns_local.length; j++)
						temp[j] = columns_local[j];

					columns_local = new String[i+1];

					for(int j = 0; j < temp.length; j++)
						columns_local[j] = temp[j];

					columns_local[i] = line.substring(line.indexOf(',',lastlocation)+1,line.indexOf(',',line.indexOf(',',lastlocation)+1));
					//Record the ith column for every pair of grading, e.g: Case, 10,

					lastlocation = line.indexOf(',',line.indexOf(',',lastlocation)+1)+1;
					//Update the lastlocation like this: Lab 01, 10,
					//					     ^
				}
				return columns_local;	
               		}
			else{
				System.out.println("No content in the chosen file!");
				return columns_local;
			}
		}

		public static String InputFile (){
			String filename = null;
			Scanner input;		
       			System.out.println("What is the file called? (no '.txt is needed')");
       			input = new Scanner(System.in);
      			filename = input.next() + ".txt";
			return filename;
		}

		public static Student[] readstudents(String filename, double[] w, int num_of_parameter) throws Exception{
			Student[] students_local = new Student[1];
			Student[] temp;
			BufferedReader reader;
            		reader = new BufferedReader(new FileReader(filename));
            		String line= reader.readLine();
			String stu_now = reader.readLine();
			for(int i = 0; stu_now != null; i++){
				int current_pos = 0;
				temp = new Student[students_local.length];

				for(int j = 0; j < students_local.length; j++)
					temp[j] = students_local[j];

				students_local = new Student[i+1];
			
				for(int j = 0; j < temp.length; j++)
					students_local[j] = temp[j];

				students_local[i] = new Student();

				students_local[i].stuID = stu_now.substring(current_pos,stu_now.indexOf(',',current_pos+1));
				current_pos = stu_now.indexOf(',',current_pos);
				//Record the stuID for the ith student

				students_local[i].stuName = stu_now.substring(current_pos+1,stu_now.indexOf(',',current_pos+1));
				current_pos = stu_now.indexOf(',',current_pos+1);
				//Record the stuName for the ith student
				
				students_local[i].assignments = new double[num_of_parameter];
				int l;
				for(l = 0; l < num_of_parameter - 1; l++){
					students_local[i].assignments[l] = Double.valueOf(stu_now.substring(stu_now.indexOf(',',current_pos)+1,stu_now.indexOf(',',current_pos+1)));
					current_pos = stu_now.indexOf(',',current_pos+1);
				}
				students_local[i].assignments[l] = Double.valueOf(stu_now.substring(stu_now.indexOf(',',current_pos)+1,stu_now.length()));
				//Record the assignments for the ith student

				students_local[i].overall = 0;
				for(int k = 0; k<num_of_parameter; k++){
					students_local[i].overall += students_local[i].assignments[k] / 100 * w[k];
				}
				stu_now = reader.readLine();
				//Calculate the overall for the students

				students_local[i].rank = 1;
				//Initialize the ranks of all the students
			}
			
			for(int i =0; i < students_local.length; i++){
				for(int k = 0; k < students_local.length; k++){
					if(students_local[i].overall < students_local[k].overall){students_local[i].rank++;}
				}
			}				
			//Calculate the rank for the students
			return students_local;
		}

		public static double[] calculate_avg(Student[] students_local, int num_of_parameter){
			double[] sum = new double[num_of_parameter + 1];
			double[] avg = new double[num_of_parameter + 1];
			int i;
			for(i = 0; i < num_of_parameter; i++){
				for(int k = 0; k < students_local.length; k++){sum[i] += students_local[k].assignments[i];}
				avg[i] = sum[i] / students_local.length;
			}
			
			for(int k = 0; k < students_local.length; k++){sum[i] += students_local[k].overall;}
			avg[i] = sum[i] / students_local.length;
			return avg;
		}

		public static void print_result(String[] c, Student[] s, double[] a){
			print_title(c);

			for(int i = 0; i < s.length; i++){print_line(s[i]);}

			print_avg(a);
		}

		public static void print_title(String[] cols){
			System.out.printf("\n%-8s ", "ID");
			System.out.printf("%-20s ", "Name");
			
			for(int i = 0; i < cols.length; i++){
			System.out.printf("%-6s ", cols[i]);
			}

			System.out.printf("%-6s ", "Overall");
			System.out.printf("%-6s ", "Rank");

		}

		public static void print_line(Student stus){
			System.out.printf("\n%-8s ", stus.stuID);
			System.out.printf("%-20s ", stus.stuName);
			
			for(int i = 0; i < stus.assignments.length; i++){
			System.out.printf("%-6.2f ", stus.assignments[i]);
			}

			System.out.printf("%-7.2f ", stus.overall);
			System.out.printf("%-6d ", stus.rank);

		}

		public static void print_avg(double[] avgs){
			System.out.printf("\n%-8s ", "");
			System.out.printf("%20s ", "Average:");
			
			for(int i = 0; i < avgs.length; i++){
			System.out.printf("%-6.2f ", avgs[i]);
			}
			
			System.out.print("\n");
		}
	}
