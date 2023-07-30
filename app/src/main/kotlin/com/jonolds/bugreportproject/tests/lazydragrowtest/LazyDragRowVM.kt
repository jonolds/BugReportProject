package com.jonolds.bugreportproject.tests.lazydragrowtest

import androidx.compose.runtime.Stable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.jonolds.bugreportproject.utils.move
import kotlin.random.Random

@Stable
class LazyDragRowVM: ViewModel() {

	val rando = Random(System.nanoTime())

	private val _colors: MutableList<Pair<Color, Dp>> = colors30
		.map { it to (75.dp * rando.nextDouble(.25, 1.0).toFloat()) }
		.toMutableStateList()
	val colors: List<Pair<Color, Dp>> = _colors



	fun move(from: Int, to: Int) {
		_colors.move(from, to)
	}

	fun removeElem(key: Any) {
		val elem = _colors.first { it.first.toArgb() == key }
		_colors.remove(elem)
	}

	fun removeByIndex(i: Int) {
		_colors.removeAt(i)
	}


	fun randomColor(): Color =
		colors[rando.nextInt(colors.size)].first


	private fun Color.hexString(): String =
		Integer.toHexString(this.toArgb()).uppercase()

	fun printShuffledColors() {
		println("val colors160 = listOf(")

		val shuffled = colors160.shuffled()

		for (color in shuffled)
			println("\tColor(0x${color.hexString()}),")

		println(")")
	}

}


val colors30 = listOf(
	Color(0xFF9E192F),
	Color(0xFFD10AC3),
	Color(0xFF09177C),
	Color(0xFFB50F07),
	Color(0xFF36A810),
	Color(0xFF0FAF4F),
	Color(0xFFDD0021),
	Color(0xFF9AFF3D),
	Color(0xFFDDB42C),
	Color(0xFF017A70),
	Color(0xFF007F0C),
	Color(0xFF096789),
	Color(0xFF002D60),
	Color(0xFF9347EF),
	Color(0xFF02698C),
	Color(0xFF4E64DB),
	Color(0xFF4ED37C),
	Color(0xFFCD4DDD),
	Color(0xFFD73BE2),
	Color(0xFF0D2587),
	Color(0xFF2F2FCE),
	Color(0xFFB611C1),
	Color(0xFF2E9FCC),
	Color(0xFFDD8416),
	Color(0xFFE50B3A),
	Color(0xFFAA222F),
	Color(0xFF490A9B),
	Color(0xFFEF15EC),
	Color(0xFFE5067D),
	Color(0xFF306CA8),
)


val colors160 = listOf(
	Color(0xFF9E192F),
	Color(0xFFD10AC3),
	Color(0xFF09177C),
	Color(0xFFB50F07),
	Color(0xFF36A810),
	Color(0xFF0FAF4F),
	Color(0xFFDD0021),
	Color(0xFF9AFF3D),
	Color(0xFFDDB42C),
	Color(0xFF017A70),
	Color(0xFF007F0C),
	Color(0xFF096789),
	Color(0xFF002D60),
	Color(0xFF9347EF),
	Color(0xFF02698C),
	Color(0xFF4E64DB),
	Color(0xFF4ED37C),
	Color(0xFFCD4DDD),
	Color(0xFFD73BE2),
	Color(0xFF0D2587),
	Color(0xFF2F2FCE),
	Color(0xFFB611C1),
	Color(0xFF2E9FCC),
	Color(0xFFDD8416),
	Color(0xFFE50B3A),
	Color(0xFFAA222F),
	Color(0xFF490A9B),
	Color(0xFFEF15EC),
	Color(0xFFE5067D),
	Color(0xFF306CA8),
	Color(0xFF053B6D),
	Color(0xFF687505),
	Color(0xFF91A50B),
	Color(0xFF005B6D),
	Color(0xFFB5110E),
	Color(0xFF3C8E05),
	Color(0xFF5BF76D),
	Color(0xFFE062D5),
	Color(0xFFE2161D),
	Color(0xFF19638E),
	Color(0xFFDB0F6B),
	Color(0xFF0D3E87),
	Color(0xFF0A2E72),
	Color(0xFF931024),
	Color(0xFF041666),
	Color(0xFFE56788),
	Color(0xFF009965),
	Color(0xFFAF0A18),
	Color(0xFF0D1587),
	Color(0xFFC1306A),
	Color(0xFF4D2DC4),
	Color(0xFF3B0191),
	Color(0xFFD84993),
	Color(0xFF044E6D),
	Color(0xFF30D1A0),
	Color(0xFF3F0070),
	Color(0xFFEF0E8A),
	Color(0xFF12246D),
	Color(0xFFA03B10),
	Color(0xFF6A9308),
	Color(0xFF0B8C0D),
	Color(0xFF66E86E),
	Color(0xFF8C1301),
	Color(0xFF42E565),
	Color(0xFF5A058C),
	Color(0xFF0C874D),
	Color(0xFF405BC4),
	Color(0xFF09187A),
	Color(0xFFB46CF7),
	Color(0xFF1E0A93),
	Color(0xFF9B2001),
	Color(0xFF20C1B9),
	Color(0xFF4BD84B),
	Color(0xFF01991F),
	Color(0xFF4A06A8),
	Color(0xFFE55257),
	Color(0xFFB1FF44),
	Color(0xFF9E0728),
	Color(0xFF064B7F),
	Color(0xFF4AF7E6),
	Color(0xFFBF6C31),
	Color(0xFFB20805),
	Color(0xFF060863),
	Color(0xFF2B8C08),
	Color(0xFF037C48),
	Color(0xFFE29E16),
	Color(0xFF5F0A9B),
	Color(0xFF9E214A),
	Color(0xFF890600),
	Color(0xFF358908),
	Color(0xFF70890B),
	Color(0xFF047008),
	Color(0xFFC48A03),
	Color(0xFFB74826),
	Color(0xFF02307F),
	Color(0xFFB4D14B),
	Color(0xFF1D06D1),
	Color(0xFF056D6B),
	Color(0xFFEDEF56),
	Color(0xFF0D9963),
	Color(0xFF0C8733),
	Color(0xFF2CBA71),
	Color(0xFFD1A330),
	Color(0xFFE25D9D),
	Color(0xFF91020C),
	Color(0xFFE25C09),
	Color(0xFFB50948),
	Color(0xFFF9C131),
	Color(0xFF61DD61),
	Color(0xFF74DD63),
	Color(0xFF0C9B62),
	Color(0xFFE55B6E),
	Color(0xFFF2691F),
	Color(0xFF0EA561),
	Color(0xFFF925F9),
	Color(0xFF688702),
	Color(0xFF9CF76F),
	Color(0xFF55EDBD),
	Color(0xFFFF8356),
	Color(0xFF15B21D),
	Color(0xFFC4058B),
	Color(0xFF960325),
	Color(0xFF076C91),
	Color(0xFF261A7A),
	Color(0xFF620D93),
	Color(0xFF005E5C),
	Color(0xFF64EADA),
	Color(0xFFDB00A4),
	Color(0xFFD140B1),
	Color(0xFF035E75),
	Color(0xFF014675),
	Color(0xFFD13AB8),
	Color(0xFF04555E),
	Color(0xFF0B4775),
	Color(0xFF4C0570),
	Color(0xFFDE3DFF),
	Color(0xFFAF4533),
	Color(0xFFED5EA8),
	Color(0xFF8D9609),
	Color(0xFFE810B2),
	Color(0xFF031863),
	Color(0xFFE83775),
	Color(0xFF089339),
	Color(0xFF96073E),
	Color(0xFF1A0175),
	Color(0xFFD30480),
	Color(0xFFF25E4B),
	Color(0xFF871805),
	Color(0xFF55D1D6),
	Color(0xFF303F93),
	Color(0xFFB609EA),
	Color(0xFFE81B8C),
	Color(0xFFC1C103),
	Color(0xFF20058C),
	Color(0xFF4FD626),
	Color(0xFF66D6E8),
	Color(0xFF4747ED),
	Color(0xFF52F96E),
	Color(0xFF559E0C),
	Color(0xFFD442F4),
)

