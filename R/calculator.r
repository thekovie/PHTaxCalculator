#   ******************
#   Last names: ARIZALA, ASPECTO, MARTINEZ, NIÑO
#   Language: R
#   Paradigm(s): Functional, Procedural
#   ******************

cat("Input salary: ")
input <- readLines('stdin', n = 1)
salary <- as.numeric(input)
setwd('../R/')

# pag-ibig contribution
pagibig_table <- read.csv('../resources/pagibig_tax.csv')
x <- subset(pagibig_table, min <= salary & max >= salary | salary > min & max == -1)

pagibig <- salary * x$rate
if (pagibig > 100) {
    pagibig <- 100
}
contributions <- pagibig
cat('Pag-Ibig:', pagibig, '\n')

# sss contribution
sss_table <- read.csv('../resources/sss_table.csv')
x <- subset(sss_table, min <= salary & max >= salary | salary > min & max == -1)

sss <- x$ee
contributions <- contributions + sss
cat('SSS:', sss, '\n')

# philhealth contribution
philhealth_table <- read.csv('../resources/philhealth_table.csv')
x <- subset(philhealth_table, year == 2023)

if (salary >= x$max_salary) {
    philhealth <- x$max_salary * 2
} else if (salary <= x$min_salary) {
    philhealth <- x$min_salary  
} else {
    philhealth <- salary
}
philhealth <- (philhealth * x$rate[1]) / 2
contributions <- contributions + philhealth
cat('PhilHealth:', philhealth, '\n')

# income tax
income_tax_table <- read.csv('../resources/income_tax_table.csv')
taxable <- salary - contributions
x <- subset(income_tax_table, min_range <= taxable & max_range >= taxable | taxable > min_range & max_range == -1)

income_tax <- (taxable - x$min_range) * x$percentage + x$additional
deductions <- contributions + income_tax

cat('Total contributions:', contributions, '\n')
cat('Income tax:',income_tax, '\n')
cat('Net pay after tax:', salary - income_tax, '\n')
cat('Total deductions:', deductions, '\n')
cat('Net pay after deductions:', salary - deductions, '\n')