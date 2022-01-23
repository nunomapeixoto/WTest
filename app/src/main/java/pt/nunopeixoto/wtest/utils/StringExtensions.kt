package pt.nunopeixoto.wtest.utils

import java.text.Normalizer

//Extensão de função String para devovler a string normalizada
fun String.removeNonSpacingMarks() =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("\\p{Mn}+".toRegex(), "")