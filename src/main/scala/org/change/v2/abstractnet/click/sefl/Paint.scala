package org.change.v2.abstractnet.click.sefl

/**
 * Author: Radu Stoenescu
 * Don't be a stranger,  symnetic.7.radustoe@spamgourmet.com
 *
 * http://www.read.cs.ucla.edu/click/elements/paint
 */
import org.change.v2.abstractnet.generic.{ConfigParameter, ElementBuilder, GenericElement, Port}
import org.change.v2.analysis.expression.concrete.ConstantValue
import org.change.v2.analysis.processingmodels.instructions.{InstructionBlock, Assign, Forward}
import org.change.v2.analysis.processingmodels.{LocationId, Instruction}

class Paint(name: String,
                   inputPorts: List[Port],
                   outputPorts: List[Port],
                   configParams: List[ConfigParameter])
  extends GenericElement(name,
    "Paint",
    inputPorts,
    outputPorts,
    configParams) {

  override def instructions: Map[LocationId, Instruction] = Map(
    // The Paint element has only one input port and its name is
    // generated by the inputPortName method (because we want this  name to be unique etc.)
    (inputPortName(0), if (configParams.length == 2)
      InstructionBlock(
        // If the optional ANNO param is present, then we assign this symbol too
        Assign("COLOR", ConstantValue(configParams(0).value.toInt)),
        Assign("ANNO", ConstantValue(configParams(1).value.toInt)),
        Forward(outputPortName(0))
      ) else InstructionBlock(
      // If only the COLOR param is present, then no ANNO assignment.
      Assign("COLOR", ConstantValue(configParams(0).value.toInt)),
      Forward(outputPortName(0))
    ))
  )
}

class PaintBuilder(name: String)
  extends ElementBuilder(name, "Paint") {

  addInputPort(Port())
  addOutputPort(Port())

  override def buildElement: GenericElement = {
    new Paint(name, getInputPorts, getOutputPorts, getConfigParameters)
  }

  override def handleConfigParameter(paramString: String): ElementBuilder = {
    super.handleConfigParameter(paramString)
    addOutputPort(Port(Some(paramString)))
  }
}

object Paint {

  private var unnamedCount = 0

  private val genericElementName = "Paint"

  private def increment {
    unnamedCount += 1
  }

  def getBuilder(name: String): PaintBuilder = {
    increment ; new PaintBuilder(name)
  }

  def getBuilder: PaintBuilder =
    getBuilder(s"$genericElementName-$unnamedCount")
}
