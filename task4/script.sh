#!/bin/bash -l
# created: Dec 4, 2014 5:43 PM
# author: Put your name here

# the following comment lines are parameters to the batch system:

#SBATCH -J myjob_jydemoXX
#SBATCH -o out.txt
#SBATCH -e err.txt
#SBATCH -n 96
#SBATCH -p parallel
#SBATCH -t 00:05:00
#SBATCH --mail-type=END
#SBATCH --mail-user=yourmailaddress@provider.tld

# some commands to manage this batch script
#   submission command
#     sbatch [script-file]
#   status command
#     squeue -u $USER
#   termination command
#     scancel [jobid]

# For more information
#   man sbatch
#   more examples in Taito guide in
#   http://research.csc.fi/taito-user-guide

# example run command, change this to the executable which you want to run
srun ~/yanjuntest/source

# This script will print some usage statistics to the
# end of file: out.txt
# Use that to improve your resource request estimate
# on later jobs.
used_slurm_resources.bash
