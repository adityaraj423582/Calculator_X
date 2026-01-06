package com.example.calculatorx.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.math.PI
import kotlin.math.E

class CalculatorViewModel : ViewModel() {

    var state by mutableStateOf(CalculatorState())
        private set

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Symbol -> enterSymbol(action.symbol)
            is CalculatorAction.Delete -> delete()
            is CalculatorAction.Clear -> state = CalculatorState(history = state.history)
            is CalculatorAction.Calculate -> calculate()
            is CalculatorAction.Negate -> performNegation()
        }
    }

    private fun performNegation() {
        val expr = state.expression
        if (expr.isNotEmpty()) {
            if (expr.startsWith("-")) {
                state = state.copy(expression = expr.removePrefix("-"))
            } else {
                state = state.copy(expression = "-$expr")
            }
        }
    }

    private fun enterNumber(number: Int) {
        state = state.copy(expression = state.expression + number)
    }

    private fun enterSymbol(symbol: String) {
        // Smart handling for scientific functions
        val newSymbol = when(symbol) {
            "sin", "cos", "tan", "log", "ln", "sqrt" -> "$symbol("
            "π" -> "pi"
            "e" -> "e"
            "x^y" -> "^"
            "x²" -> "^2"
            else -> symbol
        }
        state = state.copy(expression = state.expression + newSymbol)
    }

    private fun delete() {
        if (state.expression.isNotEmpty()) {
            state = state.copy(expression = state.expression.dropLast(1))
        }
    }

    private fun calculate() {
        var expression = state.expression
        if (expression.isEmpty()) return

        try {
            // Fix generic symbols for exp4j
            expression = expression.replace("×", "*")
                .replace("÷", "/")
                .replace("pi", PI.toString())
                .replace("e", E.toString())

            // Auto-close parentheses if missing
            val openCount = expression.count { it == '(' }
            val closeCount = expression.count { it == ')' }
            expression += ")".repeat(openCount - closeCount)

            val result = ExpressionBuilder(expression).build().evaluate()
            val resultString = result.toString().removeSuffix(".0")

            val newHistory = state.history + "${state.expression} = $resultString"
            state = state.copy(expression = resultString, history = newHistory)
        } catch (e: Exception) {
            state = state.copy(result = "Error")
        }
    }
}

data class CalculatorState(
    val expression: String = "",
    val result: String = "",
    val history: List<String> = emptyList()
)

sealed class CalculatorAction {
    data class Number(val number: Int) : CalculatorAction()
    data class Symbol(val symbol: String) : CalculatorAction()
    object Clear : CalculatorAction()
    object Delete : CalculatorAction()
    object Calculate : CalculatorAction()
    object Negate : CalculatorAction()
}