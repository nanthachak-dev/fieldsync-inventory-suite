package com.example.fieldsync_inventory_app.util.number

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Formats a numeric string with thousand separators
 * Example: "12000.33" -> "12,000.33"
 */
private fun formatWithThousandSeparator(input: String): String {
    if (input.isEmpty()) return ""
    
    // Split by decimal point
    val parts = input.split(".")
    val integerPart = parts[0]
    val decimalPart = if (parts.size > 1) parts[1] else ""
    
    // Add thousand separators to integer part
    val formattedInteger = integerPart.reversed()
        .chunked(3)
        .joinToString(",")
        .reversed()
    
    // Combine with decimal part
    return if (decimalPart.isNotEmpty()) {
        "$formattedInteger.$decimalPart"
    } else if (input.endsWith(".")) {
        "$formattedInteger."
    } else {
        formattedInteger
    }
}

/**
 * Visual transformation that adds thousand separators to numeric input
 */
object ThousandSeparatorTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val formattedText = formatWithThousandSeparator(originalText)
        
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // Count commas before this position in formatted text
                val textBeforeOffset = originalText.take(offset)
                val formattedBeforeOffset = formatWithThousandSeparator(textBeforeOffset)
                return formattedBeforeOffset.length
            }
            
            override fun transformedToOriginal(offset: Int): Int {
                // Remove commas to get original offset
                val formattedText = formatWithThousandSeparator(originalText)
                var originalOffset = 0
                var transformedOffset = 0
                
                for (char in formattedText) {
                    if (transformedOffset >= offset) break
                    if (char != ',') originalOffset++
                    transformedOffset++
                }
                
                return originalOffset
            }
        }
        
        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}
