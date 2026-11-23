import scala.io.StdIn

case class Gasto(id: Int, mes: Int, dia: Int, categoria: String, centimos: Int)
case class Ingreso(id: Int, mes: Int, dia: Int, centimos: Int)

object GestionFinanciera {
  // ---- Parámetros (montos en céntimos) ----
  val Comida = "Comida"; val Transporte = "Transporte"; val Entretenimiento = "Entretenimiento"
  val Servicios = "Servicios"; val Otros = "Otros"
  val categorias = List(Comida, Transporte, Entretenimiento, Servicios, Otros)
  val recortables = List(Comida, Transporte, Entretenimiento, Otros) // orden de desempate
  val variables = categorias.filter(_ != Servicios) // Servicios es gasto fijo
  val categoriasHormiga = List(Comida, Otros)
  val topes: Map[String, Int] =
    Map(Comida -> 20000, Transporte -> 8000, Entretenimiento -> 12000, Servicios -> 20000, Otros -> 6000)

  val mesCorte = 9; val diaCorte = 20; val diasMes = 30
  val diasRestantes = diasMes - diaCorte
  val diasVentana = 7
  val inicioVentana = diaCorte - diasVentana + 1
  val mesBase = 8
  val limiteMicro = 1000; val minMicros = 5
  val factorDesv = 2
  val saldoInicial = 9000; val ingresoRestante = 10000
  val pctRecorte = 0.20

  // ---- Datos ----
  val ingresos: List[Ingreso] = List(
    Ingreso(1,8,1,55000), Ingreso(31,8,21,10000),
    Ingreso(44,9,1,55000), Ingreso(64,9,12,10000))

  val gastos: List[Gasto] = List(
    Gasto(2,8,1,Comida,980), Gasto(3,8,2,Comida,920), Gasto(4,8,2,Transporte,500),
    Gasto(5,8,3,Comida,700), Gasto(6,8,3,Otros,1950), Gasto(7,8,4,Transporte,550),
    Gasto(8,8,5,Comida,830), Gasto(9,8,5,Servicios,3500), Gasto(10,8,6,Comida,1340),
    Gasto(11,8,7,Transporte,450), Gasto(12,8,7,Servicios,8000), Gasto(13,8,8,Comida,960),
    Gasto(14,8,8,Entretenimiento,2000), Gasto(15,8,9,Comida,1250), Gasto(16,8,9,Otros,650),
    Gasto(17,8,10,Transporte,500), Gasto(18,8,10,Servicios,6000), Gasto(19,8,11,Comida,1080),
    Gasto(20,8,12,Comida,1500), Gasto(21,8,13,Transporte,450), Gasto(22,8,13,Otros,2000),
    Gasto(23,8,14,Comida,1060), Gasto(24,8,15,Transporte,400), Gasto(25,8,15,Entretenimiento,2500),
    Gasto(26,8,16,Comida,1100), Gasto(27,8,17,Comida,1280), Gasto(28,8,18,Otros,650),
    Gasto(29,8,19,Comida,1100), Gasto(30,8,20,Transporte,500), Gasto(32,8,21,Comida,1390),
    Gasto(33,8,22,Entretenimiento,3000), Gasto(34,8,23,Comida,1350), Gasto(35,8,24,Transporte,500),
    Gasto(36,8,25,Comida,1630), Gasto(37,8,26,Otros,1750), Gasto(38,8,27,Comida,1410),
    Gasto(39,8,28,Transporte,400), Gasto(40,8,29,Comida,1220), Gasto(41,8,29,Entretenimiento,3500),
    Gasto(42,8,30,Otros,850), Gasto(43,8,31,Comida,920), Gasto(45,9,1,Comida,1400),
    Gasto(46,9,2,Servicios,8500), Gasto(47,9,2,Comida,1350), Gasto(48,9,3,Servicios,6200),
    Gasto(49,9,3,Transporte,550), Gasto(50,9,4,Servicios,3000), Gasto(51,9,4,Comida,1200),
    Gasto(52,9,5,Entretenimiento,3500), Gasto(53,9,5,Otros,2000), Gasto(54,9,6,Comida,1550),
    Gasto(55,9,6,Transporte,450), Gasto(56,9,7,Transporte,2800), Gasto(57,9,7,Comida,1600),
    Gasto(58,9,8,Comida,1300), Gasto(59,9,8,Entretenimiento,2500), Gasto(60,9,9,Transporte,500),
    Gasto(61,9,9,Otros,1400), Gasto(62,9,10,Comida,1450), Gasto(63,9,10,Transporte,300),
    Gasto(65,9,13,Comida,3800), Gasto(66,9,13,Comida,1300), Gasto(67,9,14,Comida,750),
    Gasto(68,9,14,Transporte,300), Gasto(69,9,15,Comida,850), Gasto(70,9,15,Otros,650),
    Gasto(71,9,16,Comida,900), Gasto(72,9,16,Transporte,300), Gasto(73,9,17,Comida,650),
    Gasto(74,9,17,Otros,500), Gasto(75,9,18,Comida,800), Gasto(76,9,18,Transporte,300),
    Gasto(77,9,19,Comida,950), Gasto(78,9,19,Entretenimiento,9500), Gasto(79,9,20,Comida,700),
    Gasto(80,9,20,Otros,700))

  // ---- Auxiliares ----
  // Reducir: suma de céntimos de una lista de gastos.
  def suma(gs: List[Gasto]): Int = gs.map(_.centimos).sum

  // Formato de salida: céntimos a soles con dos decimales (único punto de redondeo).
  def soles(c: Double): String = f"${c / 100}%.2f"

  // Condicional: fecha anterior o igual a la fecha de corte.
  def hastaCorte(mes: Int, dia: Int): Boolean = mes < mesCorte || (mes == mesCorte && dia <= diaCorte)

  // Filtrar + reducir: gasto de una categoría dentro de la ventana de análisis.
  def gastoVentana(cat: String): Int =
    suma(gastos.filter(g => g.categoria == cat && g.mes == mesCorte && g.dia >= inicioVentana && g.dia <= diaCorte))

  // Reducir con filtro: saldo al corte (saldo inicial + ingresos - gastos).
  def saldoActual: Int =
    saldoInicial + ingresos.filter(i => hastaCorte(i.mes, i.dia)).map(_.centimos).sum -
      suma(gastos.filter(g => hastaCorte(g.mes, g.dia)))

  // Transformar + reducir: liquidez proyectada a fin de mes.
  def liquidez: Double = {
    val variable = variables.map(gastoVentana).sum
    saldoActual + ingresoRestante - variable.toDouble / diasVentana * diasRestantes
  }

  // ---- Funcionalidades ----
  // F1: filtrar + reducir (sum) por categoría y mes, recorrido con foreach.
  def f1(): Unit = categorias.foreach { c =>
    def total(mes: Int) = suma(gastos.filter(g => g.categoria == c && g.mes == mes))
    println(f"$c%-16s agosto ${soles(total(mesBase))}%9s   septiembre ${soles(total(mesCorte))}%9s")
  }

  // F2: filtrar microgastos de la ventana + condicional de patrón.
  def f2(): Unit = categoriasHormiga.foreach { c =>
    val micros = gastos.filter(g => g.categoria == c && g.mes == mesCorte && g.dia >= inicioVentana &&
      g.dia <= diaCorte && g.centimos < limiteMicro)
    val patron = if (micros.size >= minMicros) "hay patrón" else "no hay patrón"
    println(f"$c%-16s ${micros.size} microgastos, S/ ${soles(suma(micros))}, $patron")
  }

  // F3: Map de topes + filtrar/reducir el acumulado + condicional de exceso.
  def f3(): Unit = categorias.foreach { c =>
    val acum = suma(gastos.filter(g => g.categoria == c && g.mes == mesCorte && g.dia <= diaCorte))
    val excedido = if (acum > topes(c)) acum - topes(c) else 0
    println(f"$c%-16s acumulado ${soles(acum)}%9s  tope ${soles(topes(c))}%9s  excedido ${soles(excedido)}%7s")
  }

  // F4: Map de umbrales por categoría (media + k desviaciones poblacionales) + filtrar anómalos.
  def f4(): Unit = {
    val stats: Map[String, (Int, Double, Double, Double)] = categorias.map { c =>
      val base = gastos.filter(g => g.categoria == c && g.mes == mesBase).map(_.centimos)
      val media = base.sum.toDouble / base.size
      val desv = math.sqrt(base.map(x => (x - media) * (x - media)).sum / base.size)
      c -> (base.size, media, desv, media + factorDesv * desv)
    }.toMap
    categorias.foreach { c =>
      val (n, media, desv, umbral) = stats(c)
      println(f"$c%-16s n=$n%2d  media ${soles(media)}%7s  desv ${soles(desv)}%6s  umbral ${soles(umbral)}%7s")
    }
    println("Anómalos:")
    gastos.filter(g => g.mes == mesCorte && g.dia <= diaCorte && g.centimos > stats(g.categoria)._4)
      .foreach(g => println(s"  n.º ${g.id} (${g.categoria}, ${soles(g.centimos)})"))
  }

  // F5: reducir saldo y gasto variable + condicional de riesgo.
  def f5(): Unit = {
    val variable = variables.map(gastoVentana).sum
    println(s"Saldo actual:                 S/ ${soles(saldoActual)}")
    println(s"Gasto variable de la ventana: S/ ${soles(variable)}")
    println(s"Gasto diario:                 S/ ${soles(variable.toDouble / diasVentana)}")
    println(s"Ingreso esperado restante:    S/ ${soles(ingresoRestante)}")
    println(s"Liquidez a fin de mes:        S/ ${soles(liquidez)}")
    println(if (liquidez < 0) "Riesgo: sí" else "Riesgo: no")
  }

  // Recursión: acumula el ahorro de la lista ya ordenada (categoría, ahorro, acumulado).
  def acumular(ahorros: List[(String, Double)], acum: Double): List[(String, Double, Double)] = ahorros match {
    case Nil => Nil
    case (c, a) :: resto => (c, a, acum + a) :: acumular(resto, acum + a)
  }

  // F6: transformar + sortBy + recursión de acumulado + condicional de plan.
  def f6(): Unit =
    if (liquidez >= 0) println("Sin riesgo: no hace falta plan.")
    else {
      val deficit = -liquidez
      val ahorros = recortables.map(c => (c, gastoVentana(c).toDouble / diasVentana * diasRestantes * pctRecorte))
        .sortBy(-_._2)
      val filas = acumular(ahorros, 0)
      println(s"Déficit: S/ ${soles(deficit)}")
      filas.zipWithIndex.foreach { case ((c, a, acum), i) =>
        println(f"${i + 1} $c%-16s ahorro ${soles(a)}%7s  acumulado ${soles(acum)}%7s")
      }
      val (previas, resto) = filas.span(_._3 < deficit)
      val plan = previas ++ resto.take(1)
      if (resto.isEmpty) println(s"Ni todas las categorías alcanzan: faltan S/ ${soles(deficit - filas.last._3)}")
      else println(s"Plan: ${plan.map(_._1).mkString(", ")}, ahorro S/ ${soles(plan.last._3)}")
    }

  // Clave y valor + recursión: menú que se llama a sí mismo hasta elegir 0.
  def menu(): Unit = {
    val acciones: Map[String, () => Unit] =
      Map("1" -> (() => f1()), "2" -> (() => f2()), "3" -> (() => f3()),
          "4" -> (() => f4()), "5" -> (() => f5()), "6" -> (() => f6()))
    println("\n1 F1 Totales | 2 F2 Gasto hormiga | 3 F3 Topes | 4 F4 Anomalías | 5 F5 Liquidez | 6 F6 Plan | 0 Salir")
    Option(StdIn.readLine("Opción: ")).map(_.trim).getOrElse("0") match {
      case "0" => println("Hasta luego.")
      case op =>
        acciones.get(op).fold(println("Opción inválida"))(f => f())
        menu()
    }
  }

  def main(args: Array[String]): Unit = menu()
}
