import scala.io.StdIn
import scala.util.Try
import java.util.Locale

object Gastos {
  // Modelo (case class): Gasto e Ingreso, montos en céntimos
  case class Gasto(id: Int, mes: Int, dia: Int, cat: String, cent: Int)
  case class Ingreso(id: Int, mes: Int, dia: Int, cent: Int)

  // Parámetros (val)
  val MesCorte = 9
  val DiaCorte = 20
  val DiasMes = 30
  val DiasRestantes = DiasMes - DiaCorte
  val MesBase = 8
  val PrimerDia = 1
  val UltimoDia = 31
  val DiasVentana = 7
  val IniVentana = DiaCorte - DiasVentana + 1
  val LimiteMicro = 1000
  val MinMicro = 5
  val FactorDesv = 2
  val SaldoInicial = 9000
  val IngresoRestante = 10000
  val PctRecorte = 0.20
  val CentPorSol = 100.0
  val Fijo = "Servicios"
  val Categorias = List("Comida", "Transporte", "Entretenimiento", "Servicios", "Otros")
  val Recortables = List("Comida", "Transporte", "Entretenimiento", "Otros")
  val CatsHormiga = List("Comida", "Otros")
  val Topes = Map("Comida" -> 20000, "Transporte" -> 8000, "Entretenimiento" -> 12000, "Servicios" -> 20000, "Otros" -> 6000)

  // Datos (val, List): los 80 movimientos iniciales
  val gastosIniciales = List(
    Gasto(2,8,1,"Comida",980), Gasto(3,8,2,"Comida",920), Gasto(4,8,2,"Transporte",500),
    Gasto(5,8,3,"Comida",700), Gasto(6,8,3,"Otros",1950), Gasto(7,8,4,"Transporte",550),
    Gasto(8,8,5,"Comida",830), Gasto(9,8,5,"Servicios",3500), Gasto(10,8,6,"Comida",1340),
    Gasto(11,8,7,"Transporte",450), Gasto(12,8,7,"Servicios",8000), Gasto(13,8,8,"Comida",960),
    Gasto(14,8,8,"Entretenimiento",2000), Gasto(15,8,9,"Comida",1250), Gasto(16,8,9,"Otros",650),
    Gasto(17,8,10,"Transporte",500), Gasto(18,8,10,"Servicios",6000), Gasto(19,8,11,"Comida",1080),
    Gasto(20,8,12,"Comida",1500), Gasto(21,8,13,"Transporte",450), Gasto(22,8,13,"Otros",2000),
    Gasto(23,8,14,"Comida",1060), Gasto(24,8,15,"Transporte",400), Gasto(25,8,15,"Entretenimiento",2500),
    Gasto(26,8,16,"Comida",1100), Gasto(27,8,17,"Comida",1280), Gasto(28,8,18,"Otros",650),
    Gasto(29,8,19,"Comida",1100), Gasto(30,8,20,"Transporte",500), Gasto(32,8,21,"Comida",1390),
    Gasto(33,8,22,"Entretenimiento",3000), Gasto(34,8,23,"Comida",1350), Gasto(35,8,24,"Transporte",500),
    Gasto(36,8,25,"Comida",1630), Gasto(37,8,26,"Otros",1750), Gasto(38,8,27,"Comida",1410),
    Gasto(39,8,28,"Transporte",400), Gasto(40,8,29,"Comida",1220), Gasto(41,8,29,"Entretenimiento",3500),
    Gasto(42,8,30,"Otros",850), Gasto(43,8,31,"Comida",920), Gasto(45,9,1,"Comida",1400),
    Gasto(46,9,2,"Servicios",8500), Gasto(47,9,2,"Comida",1350), Gasto(48,9,3,"Servicios",6200),
    Gasto(49,9,3,"Transporte",550), Gasto(50,9,4,"Servicios",3000), Gasto(51,9,4,"Comida",1200),
    Gasto(52,9,5,"Entretenimiento",3500), Gasto(53,9,5,"Otros",2000), Gasto(54,9,6,"Comida",1550),
    Gasto(55,9,6,"Transporte",450), Gasto(56,9,7,"Transporte",2800), Gasto(57,9,7,"Comida",1600),
    Gasto(58,9,8,"Comida",1300), Gasto(59,9,8,"Entretenimiento",2500), Gasto(60,9,9,"Transporte",500),
    Gasto(61,9,9,"Otros",1400), Gasto(62,9,10,"Comida",1450), Gasto(63,9,10,"Transporte",300),
    Gasto(65,9,13,"Comida",3800), Gasto(66,9,13,"Comida",1300), Gasto(67,9,14,"Comida",750),
    Gasto(68,9,14,"Transporte",300), Gasto(69,9,15,"Comida",850), Gasto(70,9,15,"Otros",650),
    Gasto(71,9,16,"Comida",900), Gasto(72,9,16,"Transporte",300), Gasto(73,9,17,"Comida",650),
    Gasto(74,9,17,"Otros",500), Gasto(75,9,18,"Comida",800), Gasto(76,9,18,"Transporte",300),
    Gasto(77,9,19,"Comida",950), Gasto(78,9,19,"Entretenimiento",9500), Gasto(79,9,20,"Comida",700),
    Gasto(80,9,20,"Otros",700))
  val ingresosIniciales = List(Ingreso(1,8,1,55000), Ingreso(31,8,21,10000), Ingreso(44,9,1,55000), Ingreso(64,9,12,10000))

  // Auxiliar (F7): pide un dato hasta que `convertir` lo acepte (None = inválido); sin más entrada devuelve None
  def pedir[A](prompt: String, convertir: String => Option[A]): Option[A] =
    Option(StdIn.readLine(prompt)).map(_.trim) match {
      case None => None
      case Some(texto) => convertir(texto) match {
        case None => println("  Valor inválido, intente de nuevo."); pedir(prompt, convertir)
        case valor => valor
      }
    }

  // Auxiliar (F7): convertidor de un entero dentro de [min, max]
  def enteroEn(min: Int, max: Int)(texto: String): Option[Int] =
    Some(texto).filter(t => t.nonEmpty && t.forall(_.isDigit)).flatMap(t => Try(t.toInt).toOption)
      .filter(n => n >= min && n <= max)

  // Auxiliar (F7): convertidor de soles con hasta dos decimales a céntimos, sin pasar por Double
  val FormatoMonto = """(\d+)(?:\.(\d{1,2}))?""".r
  def monto(texto: String): Option[Int] = texto match {
    case FormatoMonto(ent, dec) =>
      Try(ent.toLong * CentPorSol.toInt + Option(dec).getOrElse("").padTo(2, '0').toInt).toOption
        .filter(c => c > 0 && c <= Int.MaxValue).map(_.toInt)
    case _ => None
  }

  // Auxiliar (F7): categoría elegida de la lista numerada
  def pedirCategoria: Option[String] = {
    val opciones = Categorias.zipWithIndex.map { case (c, i) => s"${i + 1} $c" }.mkString(" | ")
    pedir(s"Categoría ($opciones): ", enteroEn(1, Categorias.size)).map(i => Categorias(i - 1))
  }

  def main(args: Array[String]): Unit = new Gastos(gastosIniciales, ingresosIniciales).menu()
}

import Gastos._

// Los datos son inmutables: F7 no los modifica, devuelve un Gastos nuevo con el movimiento agregado
final class Gastos(gastos: List[Gasto], ingresos: List[Ingreso]) {

  // Auxiliar: imprime céntimos como soles con 2 decimales (transformar)
  def soles(c: Double): String = "%.2f".formatLocal(Locale.US, c / CentPorSol)

  // Auxiliar: filtrar los gastos de un mes entre dos días
  def enRango(mes: Int, d1: Int, d2: Int): List[Gasto] =
    gastos.filter(g => g.mes == mes && g.dia >= d1 && g.dia <= d2)

  // Auxiliar: reducir con sum los céntimos de una lista de gastos
  def suma(gs: List[Gasto]): Int = gs.map(_.cent).sum

  // Auxiliar: filtrar + reducir, total de una categoría en un mes entre dos días
  def total(cat: String, mes: Int, d1: Int, d2: Int): Int = suma(enRango(mes, d1, d2).filter(_.cat == cat))

  // Auxiliar: condicional, si la fecha es anterior o igual a la de corte
  def hastaCorte(mes: Int, dia: Int): Boolean = mes < MesCorte || (mes == MesCorte && dia <= DiaCorte)

  // Auxiliar (F4): estadísticos de agosto por categoría (n, media, desviación, umbral); transformar y reducir
  def estad(cat: String): (Int, Double, Double, Double) = {
    val base = enRango(MesBase, PrimerDia, UltimoDia).filter(_.cat == cat).map(_.cent.toDouble)
    val media = base.sum / base.size
    val desv = math.sqrt(base.map(x => (x - media) * (x - media)).sum / base.size)
    (base.size, media, desv, media + FactorDesv * desv)
  }

  // Clave y valor (Map): umbrales por categoría
  val Umbrales: Map[String, (Int, Double, Double, Double)] = Categorias.map(c => c -> estad(c)).toMap

  // Auxiliar (F5): saldo actual; reducir y filtrar
  def saldoActual: Int =
    SaldoInicial + ingresos.filter(i => hastaCorte(i.mes, i.dia)).map(_.cent).sum - suma(gastos.filter(g => hastaCorte(g.mes, g.dia)))

  // Auxiliar (F5): gasto variable de la ventana; filtrar y reducir
  def gastoVariable: Int = suma(enRango(MesCorte, IniVentana, DiaCorte).filter(_.cat != Fijo))

  // Auxiliar (F5 y F6): liquidez a fin de mes
  def liquidez: Double = saldoActual + IngresoRestante - gastoVariable.toDouble / DiasVentana * DiasRestantes

  // F1: total por categoría en agosto y septiembre (filtrar, reducir, recorrer)
  def f1(): Unit =
    Categorias.foreach(c => println(s"$c: agosto ${soles(total(c, MesBase, PrimerDia, UltimoDia))} / septiembre ${soles(total(c, MesCorte, PrimerDia, DiaCorte))}"))

  // F2: microgastos y patrón hormiga en la ventana (filtrar, condicional, recorrer)
  def f2(): Unit =
    CatsHormiga.foreach { c =>
      val micro = enRango(MesCorte, IniVentana, DiaCorte).filter(g => g.cat == c && g.cent < LimiteMicro)
      val patron = if (micro.size >= MinMicro) "hay patrón" else "no hay patrón"
      println(s"$c: ${micro.size} microgastos, S/ ${soles(suma(micro))}, $patron")
    }

  // F3: acumulado, tope y excedido (reducir, Map de topes, condicional)
  def f3(): Unit =
    Categorias.foreach { c =>
      val acum = total(c, MesCorte, PrimerDia, DiaCorte)
      val excedido = if (acum > Topes(c)) acum - Topes(c) else 0
      println(s"$c: acumulado ${soles(acum)} / tope ${soles(Topes(c))} / excedido ${soles(excedido)}")
    }

  // F4: umbral por categoría y gastos anómalos (Map de umbrales, filtrar, recorrer)
  def f4(): Unit = {
    Categorias.foreach { c =>
      val (n, media, desv, umbral) = Umbrales(c)
      println(s"$c: n=$n media ${soles(media)} desviación ${soles(desv)} umbral ${soles(umbral)}")
    }
    println("Anómalos:")
    enRango(MesCorte, PrimerDia, DiaCorte).filter(g => g.cent > Umbrales(g.cat)._4)
      .foreach(g => println(s"  n.º ${g.id} (${g.cat}, ${soles(g.cent)})"))
  }

  // F5: saldo, gasto variable, liquidez y riesgo (filtrar, reducir, condicional)
  def f5(): Unit = {
    println(s"Saldo actual ${soles(saldoActual)}")
    println(s"Gasto variable de la ventana ${soles(gastoVariable)}")
    println(s"Gasto diario ${soles(gastoVariable.toDouble / DiasVentana)}")
    println(s"Ingreso esperado restante ${soles(IngresoRestante)}")
    println(s"Liquidez a fin de mes ${soles(liquidez)}")
    println(s"Riesgo: ${if (liquidez < 0) "sí" else "no"}")
  }

  // Recursión: acumula el ahorro de la lista ordenada, devolviendo (categoría, ahorro, acumulado)
  def acumular(r: List[(String, Double)], acum: Double): List[(String, Double, Double)] = r match {
    case Nil => Nil
    case (c, a) :: t => (c, a, acum + a) :: acumular(t, acum + a)
  }

  // F6: plan de recorte (transformar, ordenar con sortBy, recursión, condicional)
  def f6(): Unit =
    if (liquidez >= 0) println("No hay riesgo: no hace falta plan")
    else {
      val deficit = -liquidez
      val ahorros = Recortables.map(c => (c, total(c, MesCorte, IniVentana, DiaCorte).toDouble / DiasVentana * DiasRestantes * PctRecorte))
      val filas = acumular(ahorros.sortBy(-_._2), 0)
      println(s"Déficit ${soles(deficit)}")
      filas.zipWithIndex.foreach { case ((c, a, ac), i) =>
        println(s"${i + 1} $c ahorro ${soles(a)} acumulado ${soles(ac)}${if (ac >= deficit && ac - a < deficit) " (cubre)" else ""}")
      }
      val plan = filas.takeWhile(f => f._3 - f._2 < deficit)
      if (filas.last._3 >= deficit) println(s"Plan: ${plan.map(_._1).mkString(" y ")}, ahorro ${soles(plan.last._3)}")
      else println(s"Ni todas las categorías alcanzan: faltan ${soles(deficit - filas.last._3)}")
    }

  // F7: registra un gasto o ingreso hasta la fecha de corte (entrada por teclado, condicional, List inmutable)
  def f7(): Gastos = {
    val id = (gastos.map(_.id) ++ ingresos.map(_.id)).max + 1
    val registro: Option[Gastos] = for {
      tipo <- pedir("Tipo (1 Gasto, 2 Ingreso): ", enteroEn(1, 2))
      mes <- pedir(s"Mes ($MesBase o $MesCorte): ", enteroEn(MesBase, MesCorte))
      ultimo = if (mes == MesCorte) DiaCorte else UltimoDia
      dia <- pedir(s"Día ($PrimerDia a $ultimo): ", enteroEn(PrimerDia, ultimo))
      categoria <- if (tipo == 1) pedirCategoria.map(Option(_)) else Some(Option.empty[String])
      cent <- pedir("Monto en soles (ej. 12.50): ", monto)
    } yield categoria.fold(new Gastos(gastos, ingresos :+ Ingreso(id, mes, dia, cent)))(c =>
      new Gastos(gastos :+ Gasto(id, mes, dia, c, cent), ingresos))
    registro match {
      case Some(nuevo) => println(s"Registrado n.º $id"); nuevo
      case None => println("\nRegistro cancelado"); this
    }
  }

  // Auxiliar (menú): dibuja el título y las opciones, una debajo de otra
  def mostrarMenu(): Unit = {
    val opciones = List(
      "1" -> "Totales por categoría",
      "2" -> "Microgastos (gasto hormiga)",
      "3" -> "Topes por categoría",
      "4" -> "Gastos anómalos",
      "5" -> "Saldo, liquidez y riesgo",
      "6" -> "Plan de recorte",
      "7" -> "Registrar gasto o ingreso",
      "0" -> "Salir")
    val doble = Console.CYAN + "=" * 40 + Console.RESET
    val simple = Console.CYAN + "-" * 40 + Console.RESET
    println(s"\n$doble")
    println(s"${Console.BOLD}      GESTIÓN FINANCIERA PERSONAL${Console.RESET}")
    println(doble)
    opciones.foreach { case (n, texto) => println(s"  ${Console.YELLOW}[$n]${Console.RESET}  $texto") }
    println(simple)
  }

  // Recursión: menú de consola; con F7 continúa con el Gastos nuevo
  def menu(): Unit = {
    mostrarMenu()
    Option(StdIn.readLine("Elige una opción: ")).map(_.trim) match {
      case Some("1") => f1(); menu()
      case Some("2") => f2(); menu()
      case Some("3") => f3(); menu()
      case Some("4") => f4(); menu()
      case Some("5") => f5(); menu()
      case Some("6") => f6(); menu()
      case Some("7") => f7().menu()
      case Some("0") | None => println("Fin")
      case _ => println("Opción inválida"); menu()
    }
  }
}
