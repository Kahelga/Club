package com.example.club.util.validation

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


fun validateSurname(surname: String): String {
    val regex = Regex("^[А-Яа-яЁёA-Za-z' -]{1,60}$")
    return when {
        surname.isEmpty() -> "Поле обязательно для заполнения."
        !regex.matches(surname) -> "Недопустимые символы. Используйте только буквы и пробелы."
        surname.startsWith(" ") || surname.endsWith(" ") -> "Недопустим ввод пробела в начале или конце."
        surname.contains("  ") -> "Недопустимо использовать несколько пробелов подряд."
        else -> ""
    }
}

fun validateName(name: String): String {
    val regex = Regex("^[А-Яа-яЁёA-Za-z' -]{1,60}$")
    return when {
        name.isEmpty() -> "Поле обязательно для заполнения."
        !regex.matches(name) -> "Недопустимые символы. Используйте только буквы и пробелы."
        name.startsWith(" ") || name.endsWith(" ") -> "Недопустим ввод пробела в начале или конце."
        name.contains("  ") -> "Недопустимо использовать несколько пробелов подряд."
        else -> ""
    }
}

fun validateMiddlename(middlename: String): String {
    val regex = Regex("^[А-Яа-яЁёA-Za-z' -]{0,60}$")
    return when {
        !regex.matches(middlename) && middlename.isNotEmpty() -> "Недопустимые символы. Используйте только буквы и пробелы."
        middlename.startsWith(" ") || middlename.endsWith(" ") -> "Недопустим ввод пробела в начале или конце."
        middlename.contains("  ") -> "Недопустимо использовать несколько пробелов подряд."
        else -> ""
    }
}

fun validateEmail(email: String): String {
    val emailRegex = Regex(
        "^(?!\\.)([a-zA-Z0-9!#$%&'*+/=?^_`{|}~.-]+)@[a-zA-Z0-9.-]+\\.[a-zA-Z]{1,}$"
    )
    return when{
        email.isEmpty() -> "Поле обязательно для заполнения."
        !emailRegex.matches(email) -> "Некорректный формат"
        else -> ""
    }
}

fun validatePassword(password: String): String {
    val passwordRegex =
        Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d\$!%*#?&]{8,}$")

    return if (password.isBlank()) {
        "Поле обязательно для заполнения."
    } else if (!passwordRegex.matches(password)) {
        "Пароль должен содержать хотя бы одну строчную букву, одну заглавную букву, одну цифру и один специальный символ, а также быть не менее 8 символов"
    } else {
        ""
    }
}
fun validatePasswordRepeat(password: String, passwordRepeat: String): String {
    return when {
        passwordRepeat.isEmpty() -> "Поле обязательно для заполнения."
        passwordRepeat != password -> "Пароли не совпадают."
        else -> ""
    }
}
fun validatePhone(phone: String): String {
    val phoneRegex = Regex("^[0-9]{10,15}$")
    if (phone.isBlank()) {
        return ""
    }
    return when {
        !phoneRegex.matches(phone) -> "Номер телефона должен содержать только цифры и быть в диапазоне от 10 до 15 цифр."
        else -> ""
    }
}

fun validateCity(city: String): String {
    if (city.isBlank()) {
        return ""
    }
    val cityRegex = Regex("^[А-Яа-яЁёA-Za-z0-9 .-]{1,60}$")
    val hasCyrillic = city.any { it in 'А'..'я' }
    val hasLatin = city.any { it in 'A'..'z' }

    return when {
        !cityRegex.matches(city) -> "Некорректный формат. Используйте только буквы, цифры и символы: - ."
        city.startsWith("-") || city.startsWith(".") || city.endsWith("-") || city.endsWith(".") ->
            "Недопустим ввод спец. символов в начале и в конце строки."
        hasCyrillic && hasLatin -> "Значение должно быть задано с использованием одного из следующих алфавитов: кириллического или латинского."
        city.contains("--") || city.contains("  ") -> "Недопустимо использовать несколько пробелов или дефисов подряд."
        else -> ""
    }
}

fun validateCardNumber(cardNumber: String): String {
    return when {
        cardNumber.isEmpty() -> "Поле обязательно для заполнения."
        !cardNumber.all { it.isDigit() } -> "Допустимы только цифры."
        cardNumber.length != 16 -> "Номер карты должен содержать 16 цифр."
        else -> ""
    }
}

fun validateCVV(cvv: String): String {
    return when {
        cvv.isEmpty() -> "Поле обязательно для заполнения."
        !cvv.all { it.isDigit() } -> "Допустимы только цифры."
        cvv.length != 3 -> "Код должен содержать 3 цифры."
        else -> ""
    }
}

fun validateExpirationDate(month: String, year: String): String {
    return when {
        month.isEmpty() || year.isEmpty() -> "Поле обязательно для заполнения."
        !month.all { it.isDigit() } || !year.all { it.isDigit() } -> "Допустимы только цифры."
        month.length != 2 || year.length != 2 -> "Срок действия должен содержать 2 цифры для месяца и 2 для года."
        month.toInt() !in 1..12 -> "Максимально допустимое число месяца 12."
        year.toInt() > 99 -> "Максимально допустимое число для года 99."
        else -> ""
    }
}
fun validateAge(birthDate: String): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val birthDateParsed = dateFormat.parse(birthDate) ?: return "Некорректная дата."
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val birthYear = Calendar.getInstance().apply { time = birthDateParsed }.get(Calendar.YEAR)

    val age = currentYear - birthYear
    return when { /*try {*/

        age < 18-> "Вы должны быть не моложе 18 лет."
          else->""
    }

}