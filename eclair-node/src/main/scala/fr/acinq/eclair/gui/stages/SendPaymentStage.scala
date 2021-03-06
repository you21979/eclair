package fr.acinq.eclair.gui.stages

import javafx.fxml.FXMLLoader
import javafx.scene.image.Image
import javafx.scene.{Parent, Scene}
import javafx.stage.{Modality, Stage, StageStyle}

import fr.acinq.eclair.Setup
import fr.acinq.eclair.gui.Handlers
import fr.acinq.eclair.gui.controllers.SendPaymentController
import grizzled.slf4j.Logging

/**
  * Created by PM on 16/08/2016.
  */
class SendPaymentStage(handlers: Handlers, setup: Setup) extends Stage() with Logging {
  initModality(Modality.WINDOW_MODAL)
  initStyle(StageStyle.DECORATED)
  getIcons().add(new Image("/gui/commons/images/eclair-square.png", false))
  setTitle("Send a Payment Request")
  setMinWidth(450)
  setWidth(450)
  setMinHeight(450)
  setHeight(450)

  // get fxml/controller
  val receivePayment = new FXMLLoader(getClass.getResource("/gui/modals/sendPayment.fxml"))
  receivePayment.setController(new SendPaymentController(handlers, this, setup))
  val root = receivePayment.load[Parent]

  // create scene
  val scene = new Scene(root)
  setScene(scene)
}
