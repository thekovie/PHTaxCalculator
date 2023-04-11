/*
    ********************
    Last names: ARIZALA, ASPECTO, MARTINEZ, NIÑO
    Language: Kotlin
    Paradigm(s): Functional and Procedural
    ********************
 */

import java.io.File
import java.time.Year
import kotlin.math.round
import kotlin.math.pow
import kotlin.math.floor
import java.text.DecimalFormat

data class incomeRange(val min: Double, val max: Double, val additional: Double, val percent: Double)
data class philhealthRange(val year: Int, val max_salary: Double, val rate: Double)
data class sssRange(val min: Double, val max: Double, val fee: Double)
data class pagibigRange(val min: Double, val max: Double, val rate: Double)

fun main() {
    val incomeTable = File("../resources/income_tax_table.csv")
    val philhealthTable = File("../resources/philhealth_table.csv")
    val sssTable = File("../resources/sss_table.csv")
    val pagibigTable = File("../resources/pagibig_tax.csv")

    val readIncome = readIncomeTable(incomeTable)
    val readPhilhealth = readPhilhealthTable(philhealthTable)
    val readSSS = readSSSTable(sssTable)
    val readPagibig = readPagibigTable(pagibigTable)

    // Input salary
    print("Enter your monthly salary: ")
    val salary = readLine()!!.toDouble()

    // Compute
    val sssFee = bankersRounding(computeSSS(salary, readSSS))
    val philhealthFee = bankersRounding(computePhilhealth(salary, readPhilhealth))
    val pagibigFee = bankersRounding(computepagIbig(salary, readPagibig))
    val totalContributions = bankersRounding(sssFee + philhealthFee + pagibigFee)

    println("\n== COMPUTATION RESULT ==\n")

    // Monthly Contributions
    println("-- Monthly Contributions --")
    println("SSS: P ${DecimalFormat("#,###.00").format(sssFee)}")
    println("Philhealth: P ${DecimalFormat("#,###.00").format(philhealthFee)}")
    println("Pag-ibig: P ${DecimalFormat("#,###.00").format(pagibigFee)}")
    println("Total Contributions: P ${DecimalFormat("#,###.00").format(totalContributions)}")

    // Tax Computation
    val incomeTax = bankersRounding(computeIncomeTax(salary, totalContributions, readIncome))
    val netPay = bankersRounding(salary - incomeTax)

    println("\n-- Monthly Contributions --")
    println("Income Tax: P ${DecimalFormat("#,###.00").format(incomeTax)}")
    println("Net Pay after Tax: P ${DecimalFormat("#,###.00").format(netPay)}")

    // Deductions
    println("\n-- Deductions --")
    println("Total Deductions: P ${DecimalFormat("#,###.00").format(totalContributions + incomeTax)}")
    println("Net Pay after Deductions: P ${DecimalFormat("#,###.00").format(netPay - totalContributions)}")
}

fun bankersRounding(number: Double): Double {
    val factor = 10.0.pow(2)
    val rounded = round(number * factor)

    return if (rounded % 2 == 0.0) {
        rounded / factor
    } else {
        val floored = floor(number * factor)
        if (floored % 2 == 0.0) {
            floored / factor
        } else {
            (floored + 1) / factor
        }
    }
}

fun readIncomeTable(file: File): List<incomeRange> {
    val reader = file.bufferedReader()

    // Skip header row
    reader.readLine()

    // Convert the CSV data to a list of Range objects
    return reader.useLines { lines ->
        lines.map { line ->
            val values = line.split(",")
            incomeRange(
                min = values[0].toDouble(),
                max = values[1].toDouble(),
                additional = values[2].toDouble(),
                percent = values[3].toDouble()
            )
        }.toList()
    }
}

fun readPhilhealthTable(file: File): List<philhealthRange> {
    val reader = file.bufferedReader()

    // Skip header row
    reader.readLine()

    // Convert the CSV data to a list of Range objects
    return reader.useLines { lines ->
        lines.map { line ->
            val values = line.split(",")
            philhealthRange(
                year = values[0].toInt(),
                max_salary = values[2].toDouble(),
                rate = values[3].toDouble()
            )
        }.toList()
    }
}

fun readSSSTable(file: File): List<sssRange> {
    val reader = file.bufferedReader()

    // Skip header row
    reader.readLine()

    // Convert the CSV data to a list of Range objects
    return reader.useLines { lines ->
        lines.map { line ->
            val values = line.split(",")
            sssRange(
                min = values[0].toDouble(),
                max = values[1].toDouble(),
                fee = values[2].toDouble()
            )
        }.toList()
    }
}

fun readPagibigTable(file: File): List<pagibigRange> {
    val reader = file.bufferedReader()

    // Skip header row
    reader.readLine()

    // Convert the CSV data to a list of Range objects
    return reader.useLines { lines ->
        lines.map { line ->
            val values = line.split(",")
            pagibigRange(
                min = values[0].toDouble(),
                max = values[1].toDouble(),
                rate = values[2].toDouble()
            )
        }.toList()
    }
}

fun computeSSS(n: Double, ranges: List<sssRange>): Double {
    return ranges.firstOrNull { range ->
        (range.min == -1.0 && n <= range.max) || // If min is -1, then n should be less than or equal to max
                (range.max == -1.0 && n >= range.min) || // If max is -1, then n should be greater than or equal to min
                (n in range.min..range.max) // Otherwise, n should be within the min and max range
    }?.fee ?: -1.0 // If no matching range is found, return -1 as an error indicator
}

fun computePhilhealth(n: Double, ranges: List<philhealthRange>): Double {
    val currentYear = Year.now().value
    val rate = ranges.filter { range -> range.year == currentYear }.first().rate
    val maxSalary = ranges.filter { range -> range.year == currentYear }.first().max_salary

    if (n <= maxSalary) {
        return n * rate / 2
    }

    return maxSalary * rate
}

fun computepagIbig(n: Double, ranges: List<pagibigRange>): Double {
    val rate =  ranges.firstOrNull { range -> range.max <= n }?.rate ?: ranges.filter { range -> range.max > n }.first().rate
    val fee = n * rate

    if (fee > 100.0) {
        return 100.0
    }
    return fee
}

fun computeIncomeTax (salary: Double, contributions: Double, ranges: List<incomeRange>): Double {

    val taxable_income = salary - contributions

    val min = ranges.firstOrNull { range ->
        (range.min == -1.0 && taxable_income <= range.max) || // If min is -1, then n should be less than or equal to max
                (range.max == -1.0 && taxable_income >= range.min) || // If max is -1, then n should be greater than or equal to min
                (taxable_income in range.min..range.max) // Otherwise, n should be within the min and max range
    }?.min ?: -1.0 // If no matching range is found, return -1 as an error indicator
    val rate = ranges.firstOrNull { range ->
        (range.min == -1.0 && taxable_income <= range.max) || // If min is -1, then n should be less than or equal to max
                (range.max == -1.0 && taxable_income >= range.min) || // If max is -1, then n should be greater than or equal to min
                (taxable_income in range.min..range.max) // Otherwise, n should be within the min and max range
    }?.percent ?: -1.0 // If no matching range is found, return -1 as an error indicator
    val additional = ranges.firstOrNull { range ->
        (range.min == -1.0 && taxable_income <= range.max) || // If min is -1, then n should be less than or equal to max
                (range.max == -1.0 && taxable_income >= range.min) || // If max is -1, then n should be greater than or equal to min
                (taxable_income in range.min..range.max) // Otherwise, n should be within the min and max range
    }?.additional ?: -1.0 // If no matching range is found, return -1 as an error indicator

    val compensation_lvl = (taxable_income - min) * rate

    return additional + compensation_lvl
}