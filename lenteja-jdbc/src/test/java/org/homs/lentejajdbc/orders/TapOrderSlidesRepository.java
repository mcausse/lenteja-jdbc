package org.homs.lentejajdbc.orders;

import org.homs.lentejajdbc.DataAccesFacade;
import org.homs.lentejajdbc.Mapable;
import org.homs.lentejajdbc.ResultSetUtils;
import org.homs.lentejajdbc.orders.ent.*;

import java.util.List;

import static org.homs.lentejajdbc.ResultSetUtils.extractRowAsMap;
import static org.homs.lentejajdbc.query.QueryObjectUtils.queryFor;

public class TapOrderSlidesRepository {

    final DataAccesFacade facade;

    public TapOrderSlidesRepository(DataAccesFacade facade) {
        this.facade = facade;
    }

    final Mapable<Patient> patientMapable = rs -> {
        var r = new Patient();
        r.id = ResultSetUtils.getInteger(rs, "ID");
        r.PatientID1 = ResultSetUtils.getString(rs, "PatientID1");
        r.FirstName = ResultSetUtils.getString(rs, "FirstName");
        r.MiddleName = ResultSetUtils.getString(rs, "MiddleName");
        r.LastName = ResultSetUtils.getString(rs, "LastName");
        r.allValues = extractRowAsMap(rs);

        r.orders = getOrdersByPatientID(r.id);
        return r;
    };
    final Mapable<Order> orderMapable = rs -> {
        var r = new Order();
        r.id = ResultSetUtils.getInteger(rs, "ID");
        r.sampleId = ResultSetUtils.getString(rs, "SampleID");
        r.allValues = extractRowAsMap(rs);

        r.containers = getContainersByOrderID(r.id);
        r.slides = getSlideByOrderID(r.id);
        r.objects = getObjectsByOrderId(r.id);
        return r;
    };
    final Mapable<Container> containerMapable = rs -> {
        var r = new Container();
        r.id = ResultSetUtils.getInteger(rs, "ID");
        r.ContainerID = ResultSetUtils.getString(rs, "ContainerID");
        r.allValues = extractRowAsMap(rs);

        r.blocks = getBlocksByContainersID(r.id);
        r.slides = getSlideByContainerID(r.id);
        r.objects = getObjectsByContainerId(r.id);
        return r;
    };
    final Mapable<Block> blockMapable = rs -> {
        var r = new Block();
        r.id = ResultSetUtils.getInteger(rs, "ID");
        r.BlockID = ResultSetUtils.getString(rs, "BlockID");
        r.allValues = extractRowAsMap(rs);

        r.slides = getSlideByBlockID(r.id);
        r.objects = getObjectsByBlockId(r.id);
        return r;
    };
    final Mapable<Slide> slideMapable = rs -> {
        var r = new Slide();
        r.id = ResultSetUtils.getInteger(rs, "ID");
        r.SlideID = ResultSetUtils.getString(rs, "SlideID");
        r.allValues = extractRowAsMap(rs);

        r.objects = getObjectsBySlideId(r.id);
        return r;
    };
    final Mapable<OrderObject> orderObjectMapable = rs -> {
        var r = new OrderObject();
        r.id = ResultSetUtils.getInteger(rs, "ID");
        r.allValues = extractRowAsMap(rs);
        return r;
    };


    public List<Patient> getPatients() {
        return facade.load(queryFor("select * from SQLUser.tPatients"), patientMapable);
    }

    List<Order> getOrdersByPatientID(int patientId) {
        return facade.load(queryFor("select * from SQLUser.tOrders where rPatients=?", patientId), orderMapable);
    }

    List<Container> getContainersByOrderID(int orderId) {
        return facade.load(queryFor("select * from SQLUser.tapOrderContainers where rOrders=?", orderId), containerMapable);
    }

    List<Block> getBlocksByContainersID(int containerId) {
        return facade.load(queryFor("select * from SQLUser.tapOrderBlocks where rapOrderContainers=?", containerId), blockMapable);
    }

    List<Slide> getSlideByBlockID(int blockId) {
        return facade.load(queryFor("select * from SQLUser.tapOrderSlides where rapOrderBlocks=?", blockId), slideMapable);
    }

    List<Slide> getSlideByContainerID(int containerId) {
        return facade.load(queryFor("select * from SQLUser.tapOrderSlides where rapOrderContainers=?", containerId), slideMapable);
    }

    List<Slide> getSlideByOrderID(int orderId) {
        return facade.load(queryFor("select * from SQLUser.tapOrderSlides where rOrders=?", orderId), slideMapable);
    }

    List<OrderObject> getObjectsByOrderId(int orderId) {
        return facade.load(queryFor("select * from SQLUser.tapOrderObjects where rOrders=?", orderId), orderObjectMapable);
    }

    List<OrderObject> getObjectsByContainerId(int containerId) {
        return facade.load(queryFor("select * from SQLUser.tapOrderObjects where rapOrderContainers=?", containerId), orderObjectMapable);
    }

    List<OrderObject> getObjectsByBlockId(int blockId) {
        return facade.load(queryFor("select * from SQLUser.tapOrderObjects where rapOrderBlocks=?", blockId), orderObjectMapable);
    }

    List<OrderObject> getObjectsBySlideId(int slideId) {
        return facade.load(queryFor("select * from SQLUser.tapOrderObjects where rapOrderSlides=?", slideId), orderObjectMapable);
    }
}