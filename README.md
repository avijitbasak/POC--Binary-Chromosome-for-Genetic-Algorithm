# POC--Binary-Chromosome-for-Genetic-Algorithm
This POC is to demonstrate the metadata based optimized approach for implementing binary chromosomes in Genetic Algorithm as presented in the article https://arxiv.org/abs/2103.04751. The concept has been implemented using Java. Due to it's structural nature the metadata based approach has been modified a little bit. Instead of retaining the chromosome length as metadata in the first element of the allele array we have retained it as a property of Chromosome class.
For simulation purpose a quadratic equation having three independent variables have been used and is referred as 'Function 1'.

Libraries Used:
apache-commons-math-v3.6.1
jcommons-1.0.23
jfreechart-1.0.19  
