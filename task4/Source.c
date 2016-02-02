#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <mpi.h>

#define ATOM_COUNT 16
#define FEATURE_COUNT 96
#define MOLECULE_COUNT 296

typedef struct atomStruct atomStruct;
struct atomStruct{
    double x,y,z;
    double vx,vy,vz;
};

typedef struct molecule molecule;
struct molecule {
     atomStruct atoms[ATOM_COUNT];
     int outcome;    
};
typedef struct dimensions_struct dimensions;
struct dimensions_struct{
	double dimension[MOLECULE_COUNT*FEATURE_COUNT];
};

/**
 * parse parses 'amount' molecules encoded in the file with name 'filename' into the molecule array.
 */
void parse(molecule res[], int amount, char *filename){
    
    FILE *fp;
    int i, moleculeNumber;
    
    int lineBufferSize = 100;
    char* lineBuffer = malloc(sizeof(char) * lineBufferSize);
    
    if ((fp = fopen(filename, "r")) == NULL) {
        fprintf(stderr, "Could not open file: %s\n", filename);
        exit(EXIT_FAILURE);
    }
    for (moleculeNumber = 0; moleculeNumber < amount; moleculeNumber++){
        //header
        if (fgets(lineBuffer, lineBufferSize, fp) == NULL){
            fprintf(stderr, "Could not read next line\n");
            exit(EXIT_FAILURE);
        }
        int * outcome = &(res[moleculeNumber].outcome);
        sscanf(lineBuffer,"Outcome: %i", outcome);
        //skip line
        if (fgets(lineBuffer, lineBufferSize, fp) == NULL){
            fprintf(stderr, "Could not read next line\n");
            exit(EXIT_FAILURE);
        }
        //read 16 atoms
        for (i = 0; i < 16; i++) {
            if (fgets(lineBuffer, lineBufferSize, fp) == NULL){
                fprintf(stderr, "Could not read next line\n");
                exit(EXIT_FAILURE);
            }
            atomStruct * a = &(res[moleculeNumber].atoms[i]);
            //    1SB2     NZ    1  -0.020   0.016  -0.029  0.5938 -0.1478 -0.3879    
            //skip first 22 characters!
            sscanf(lineBuffer  + (22 * sizeof(char)),"%lf %lf %lf %lf %lf %lf", &a->x,&a->y,&a->z,&a->vx,&a->vy,&a->vz);
        }
        //skip line
        if (fgets(lineBuffer, lineBufferSize, fp) == NULL){
            fprintf(stderr, "Could not read next line\n");
            exit(EXIT_FAILURE);
        }
    }
    free(lineBuffer);
    fclose(fp);
}
void refine(dimensions *ds, molecule molcs[], int amount){
	int iterator, iterator2 = 0, i=0;
	int jumps[6] = { 0, 0, 0, 0, 0, 0 };
	for (i = 1; i < 6; i++){
		jumps[i] += amount*ATOM_COUNT*i;
	}
	for (iterator2 = 0; iterator2 < ATOM_COUNT; iterator2++){
		for (iterator = 0; iterator < amount; iterator++){
			molecule cur;
			cur = molcs[iterator];
			// Add to a single array. Length 48 864 or 509 * 96
			ds->dimension[jumps[0]++] = cur.atoms[iterator2].vx;
			ds->dimension[jumps[1]++] = cur.atoms[iterator2].vy;
			ds->dimension[jumps[2]++] = cur.atoms[iterator2].vz;
			ds->dimension[jumps[3]++] = cur.atoms[iterator2].x;
			ds->dimension[jumps[4]++] = cur.atoms[iterator2].y;
			ds->dimension[jumps[5]++] = cur.atoms[iterator2].z;
			}
		}
	}
// we can count average too in the same function
void createGroups(double *pool[], int *outcomes[], double *groups[], int *count[], double *total[]){
	int i;
	for (i = 0; i < MOLECULE_COUNT; i++){
		double temporal;
		temporal = *pool[i];
		// Assign each element to a correct index but on different row depending on outcome
		if (*outcomes[i] == 1){
			groups[2][i] = temporal;
			*total[2] += temporal;
			count[2]++;
		}
		else if (*outcomes[i] == 0){
			groups[1][i] = temporal;
			*total[1] += temporal;
			count[1]++;
		}
		else{
			groups[0][i] = temporal;
			*total[0] += temporal;
			count[0]++;
		}
	}
}
void average(double *total[], int *count[], double *average[]){
	int i;
	for (i = 0; i < 3; i++){
		double t = *total[i];
		int c = *count[i];
		*average[i] = t / c;
	}
}
void variance(double **groups[], double *averages[], double *variances[], int *count[], double *standardDeviation[]){
	int i, j;

	double min;
	(*((long long*)&min)) = ~(1LL << 52);

	for (i = 0; i < 3; i++){
		for (j = 0; j < MOLECULE_COUNT; j++){
			double temporal;
			temporal = *groups[i][j];
			if (temporal > min){ // groups are initialized to _I64_MIN
				*variances[i] = *variances[i] + pow((*averages[i] - temporal), 2.0); // difference to average to the power of 2
			}
		}
		*variances[i] = *variances[i] / (*count[i]-1); // divide by n -1
		*standardDeviation[i] = sqrt(*variances[i]);
	}
}
void zscore(double avg, double avg2, double stdd, double *combination[], int *iter){
	*combination[*iter] = abs(avg - avg2) / stdd;
	*iter++;
}
void zscore_combination (double *combination[], double *averages[], double *standardDeviation[], int l){
	int i=0, j = l, combinator_iter = 0;
	for (i = 0; i < l; i++){
		double a, a2, stdd, i_stdd;
		a = *averages[i];
		a2 = *averages[j];
		
		stdd = *standardDeviation[i];
		i_stdd = *standardDeviation[j];

		zscore(a, a2, stdd, *combination, &combinator_iter);
		zscore(a2, a, i_stdd, *combination, &combinator_iter);
		j--;
	}
}

int main(int argc, char **argv)
{
    int i;    
	MPI_Init(&argc, &argv);
	int world_rank;
	MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);
	int world_size;
	MPI_Comm_size(MPI_COMM_WORLD, &world_size);
	double pool[MOLECULE_COUNT]; // 296
	double * theDs = NULL;
	int outcomes [MOLECULE_COUNT];
	if (world_rank == 0){
		char input_file[] = "diabatic.txt";
		molecule molecules[MOLECULE_COUNT];
		parse(molecules, MOLECULE_COUNT, input_file); // Fills the molecules array with molecules
		dimensions ds = { 0.0 };
		refine(&ds, molecules, MOLECULE_COUNT);
		theDs = ds.dimension;
		for (i = 0; i < MOLECULE_COUNT; i++){
			outcomes[i] = molecules[i].outcome;
		}
	}
	MPI_Scatter(theDs, MOLECULE_COUNT, MPI_DOUBLE, &pool, MOLECULE_COUNT, MPI_DOUBLE, 0, MPI_COMM_WORLD);
	MPI_Bcast(&outcomes, MOLECULE_COUNT, MPI_INT, 0, MPI_COMM_WORLD);

	// Arrays for calcuations 
	double total[] = { 0., 0., 0. };
	int count[] = { 0, 0, 0 };
	double averages[] = { 0., 0., 0. };
	double variances[] = { 0., 0., 0. };
	double standardDeviation[] = { 0., 0., 0. };
	double groups[3][MOLECULE_COUNT]; // Each "row" is a outcome so -1, 0 or 1

	// init array
	int r, c;
	double min;
	(*((long long*)&min)) = ~(1LL << 52); // just minimun double value
	for (r = 0; r < 3; r++){
		for (c = 0; c < MOLECULE_COUNT; c++){
			groups[r][c] = min;
		}

	}
	
	// Problem with &pool?
	createGroups(&pool, &outcomes, &groups, &count, &total); // groups by the outcomes
	// We can count the total at the same time
	average(&total, &count, &averages); // average for each result
	variance(&groups, &averages, &variances, &count, &standardDeviation); // variance and standard deviation
	// Array of 6, avgs and standard deviations 
	// MPI_Gather, similar to scatter
	double z_combination[6] = { 0 }; // used to count z-score
	zscore_combination(&z_combination, &averages, &standardDeviation, 3); // Calcs the z-score
	
	double zranks[MOLECULE_COUNT*FEATURE_COUNT] = { 0. }; // all the zscores. Problem is this?
	MPI_Gather(z_combination, MOLECULE_COUNT, MPI_DOUBLE, zranks, MOLECULE_COUNT, MPI_DOUBLE, 0, MPI_COMM_WORLD);

	if (world_rank == 0){
		FILE *f = fopen("results.txt", "w"); // Open a file with writing purposes
		//fprintf(f, text);
		int i = 0;
		for (i = 0; i < MOLECULE_COUNT*FEATURE_COUNT; i=i+6){
			char buf[6][100] = { " " };
			int j = 0;
			for (j = i; j < i + 6; j++){
				sprintf(buf[j]," ",zranks[i + j]); // Should add double to char buffer
				fprintf(f, buf[j]); // Write into a file
			}
		}
	}
    return 0;
}