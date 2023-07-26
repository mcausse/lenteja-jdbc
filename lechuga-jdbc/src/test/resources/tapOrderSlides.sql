DROP SCHEMA SQLUser IF EXISTS CASCADE;
CREATE SCHEMA SQLUser AUTHORIZATION DBA;


CREATE TABLE SQLUser.tTests
(
   ID integer PRIMARY KEY NOT NULL,
   InformIsolateds BIT DEFAULT 1,
   KeepTest BIT DEFAULT 0,
   MicroType integer DEFAULT 0 NOT NULL,
   PrintLabel BIT,
   Printable BIT DEFAULT 1 NOT NULL,
   ResultsGroup varchar(6),
   ShowOrder integer,
   Status integer DEFAULT 1,
   TestAbbreviation varchar(100) NOT NULL,
   TestID varchar(25) NOT NULL,
   TestName varchar(100),
   Use integer NOT NULL,
   TS_TSDateTime varchar(14),
   TS_TSUser varchar(16)
);

CREATE TABLE SQLUser.tPatients
(
   ID integer PRIMARY KEY NOT NULL,
   Age integer,
   DateOfBirth date,
   ExtPatientID varchar(50),
   FirstName varchar(80),
   HistoryStatus varchar(1) DEFAULT 'D' NOT NULL,
   HostID integer,
   LastName varchar(80),
   MiddleAndLastName varchar(50),
   MiddleName varchar(80),
   PatientID1 varchar(50) NOT NULL,
   PatientID1Seq varchar(10),
   PatientID2 varchar(50),
   PatientID3 varchar(50),
   Photo LONGVARBINARY,
   PhotoName varchar(200),
   Prefix varchar(100),
   Race varchar(20),
   Recycled BIT DEFAULT 0,
   RecycledDateTime timestamp,
   Religion varchar(100),
   SecondSurname varchar(30),
   Sex varchar(20),
   Suffix varchar(100),
   SurNameAndName varchar(50),
   TS_TSDateTime varchar(14),
   TS_TSUser varchar(16)
);


CREATE TABLE SQLUser.tOrders
(
   ID1 integer NOT NULL,
   CaseTags varchar(500),
   CloseDate date,
   Destination varchar(50),
   Diagnosis varchar(100),
   Doctor varchar(50),
   ExtPatientID varchar(30),
   ExtRegisterDate date,
   ExtRegisterHour time,
   ExtSampleID varchar(50),
   ExtractionDate date,
   ExtractionHour time,
   FirstEditDate date,
   FirstEditHour time,
   FirstEditPrinter varchar(30),
   HasGraphic BIT DEFAULT 0,
   HistoryStatus varchar(1) DEFAULT 'D' NOT NULL,
   HostCaptureID integer,
   HostID integer,
   ID integer PRIMARY KEY NOT NULL,
   InProgress BIT DEFAULT 0,
   InstrumentSampleID varchar(50),
   LabSite varchar(50),
   LabelPending BIT DEFAULT 1,
   LastEditDate date,
   LastEditHour time,
   LastEditPrinter varchar(30),
   Microbiology integer DEFAULT 1,
   Motive varchar(50),
   Observations varchar(255),
   OnHold BIT DEFAULT 0,
   OrderStatus integer DEFAULT 1,
   Origin varchar(50),
   PeriodUnits varchar(50),
   PhysiologicalPeriod integer,
   PhysiologicalType varchar(50),
   PreOrderStatus integer,
   Priority integer DEFAULT 0,
   Recycled BIT DEFAULT 0,
   RecycledDateTime timestamp,
   RegisterDate date NOT NULL,
   RegisterHour time NOT NULL,
   RegisterYear varchar(4) NOT NULL,
   RegisteredOn date NOT NULL,
   ReportNote varchar(20000),
   STAT varchar(50),
   SampleID varchar(30) NOT NULL,
   SampleIDDate varchar(50),
   SampleIDPrefix varchar(50),
   SampleIDSeqNum integer,
   SampleIDSuffix varchar(50),
   Service varchar(50),
   Status integer,
   TypeExternalID BIT,
   Use varchar(20),
   VipOrderControl varchar(2),
   VipOrderStatus varchar(2),
   VisitNumber varchar(25),
   WARetention BIT DEFAULT 1,
   WARetentionCloseTS timestamp,
   WithDemographics BIT DEFAULT 0,
   WorkFlow varchar(50),
   lstScannedVRT varchar(32749),
   rPatients integer,
   TS_TSDateTime varchar(14),
   TS_TSUser varchar(16)
);
ALTER TABLE SQLUser.tOrders
ADD CONSTRAINT rPatients
FOREIGN KEY (rPatients)
REFERENCES SQLUser.tPatients(ID)
;

CREATE TABLE SQLUser.tapOrderContainers
(
   ID1 integer NOT NULL,
   Assigned BIT DEFAULT 0,
   CollectDate date,
   CollectHour time,
   ContainerID varchar(50),
   ContainerStatus integer,
   ExternalID varchar(50),
   HumanReadable varchar(50),
   ID integer PRIMARY KEY NOT NULL,
   OnHold BIT DEFAULT 0,
   ReceivedDate date,
   ReceivedHour time,
   ReleaseDate date,
   ReleaseTime time,
   Sequence varchar(100),
   rOrders integer,
   rapAnatomicSites integer,
   rapFacilities integer,
   rapSurgicalProcedure integer,
   rapTissues integer,
   TS_TSDateTime varchar(14),
   TS_TSUser varchar(16)
);
/*
ALTER TABLE "SQLUser"."tapOrderContainers"
ADD CONSTRAINT FKeySurgicalProcedure
FOREIGN KEY (rapSurgicalProcedure)
REFERENCES "SQLUser"."tapSurgicalProcedure"(ID)
;
*/
ALTER TABLE SQLUser.tapOrderContainers
ADD CONSTRAINT FKeyOrders
FOREIGN KEY (rOrders)
REFERENCES SQLUser.tOrders(ID)
;
/*
    ALTER TABLE "SQLUser"."tapOrderContainers"
    ADD CONSTRAINT FKeyAnatomicSites
    FOREIGN KEY (rapAnatomicSites)
    REFERENCES "SQLUser"."tapAnatomicSites"(ID)
    ;
    ALTER TABLE "SQLUser"."tapOrderContainers"
    ADD CONSTRAINT FKeyTissues
    FOREIGN KEY (rapTissues)
    REFERENCES "SQLUser"."tapTissues"(ID)
    ;
    ALTER TABLE "SQLUser"."tapOrderContainers"
    ADD CONSTRAINT FKeyFacilities
    FOREIGN KEY (rapFacilities)
    REFERENCES "SQLUser"."tapFacilities"(ID)
    ;
*/


CREATE TABLE SQLUser.tapOrderBlocks
(
   ID1 integer NOT NULL,
   Assigned BIT DEFAULT 0,
   BlockID varchar(50),
   ExternalID varchar(50),
   HumanReadable varchar(50),
   ID integer PRIMARY KEY NOT NULL,
   OnHold BIT DEFAULT 0,
   Printed BIT DEFAULT 0,
   Sequence varchar(100),
   StainingRequestComment varchar(150),
   StainingRequestProtocol varchar(50),
   Status integer,
   TissueSubtype varchar(50),
   TissueSubtypeDesc varchar(100),
   Virtual BIT DEFAULT 0,
   rapOrderBlocks integer,
   rapOrderContainers integer,
   TS_TSDateTime varchar(14),
   TS_TSUser varchar(16)
)
;
ALTER TABLE SQLUser.tapOrderBlocks
ADD CONSTRAINT FKeyapOrderContainers
FOREIGN KEY (rapOrderContainers)
REFERENCES SQLUser.tapOrderContainers(ID)
;
ALTER TABLE SQLUser.tapOrderBlocks
ADD CONSTRAINT FKeyapOrderBlocks
FOREIGN KEY (rapOrderBlocks)
REFERENCES SQLUser.tapOrderBlocks(ID)
;


CREATE TABLE SQLUser.tapOrderSlides
(
   ID1 integer NOT NULL,
   ActionCode varchar(50),
   AnalysisScoreData varchar(2147483647),
   Assigned BIT DEFAULT 0,
   AutoPrinted BIT,
   CloneType varchar(50),
   ControlSlideType varchar(50),
   ControlSlideTypeDescription varchar(255),
   ExternalID varchar(50),
   HostID varchar(50),
   HostVersion varchar(50),
   HumanReadable varchar(50),
   ID integer PRIMARY KEY NOT NULL,
   InProgress BIT DEFAULT 0,
   Instrument varchar(50),
   KeyCode varchar(50),
   LabelPrinted varchar(14),
   OnHold BIT DEFAULT 0,
   OperationCode varchar(50),
   PanelID varchar(50),
   ProtocolIdentifier varchar(100),
   ReagentInfo varchar(255),
   ReagentLot varchar(50),
   RecutText varchar(50),
   ReportExcluded BIT,
   ReportFovNote varchar(150),
   ReportNote varchar(20000),
   Reprint BIT,
   RescanComment varchar(150),
   RescanReason varchar(50),
   Rescanned BIT,
   RunEstimatedTime varchar(50),
   RunNumber varchar(50),
   RunTime varchar(50),
   ScoringType varchar(50),
   Sequence varchar(100),
   ShortName varchar(50),
   SlideID varchar(50),
   SlideLevel varchar(50),
   SlidePosition varchar(50),
   SlideScanStatus varchar(50),
   SlideWSAStatus varchar(50),
   StainEndDate date,
   StainEndHour time,
   StainStartDate date,
   StainStartHour time,
   StainerEffectiveType varchar(255),
   StainerFriendlyName varchar(50),
   StainerSerialNumber varchar(50),
   StainingBatchID varchar(50),
   Status integer,
   TemplateDisplayText varchar(50),
   Unstained BIT DEFAULT 0,
   Vendor varchar(50),
   Virtual BIT DEFAULT 0,
   algorithmId varchar(50),
   keyParameterName varchar(100),
   qualitativeResult varchar(100),
   quantitativeResult varchar(357),
   rOrders integer,
   rTests integer,
   rapOrderBlocks integer,
   rapOrderContainers integer,
   TS_TSDateTime varchar(14),
   TS_TSUser varchar(16)
)
;
ALTER TABLE SQLUser.tapOrderSlides
ADD CONSTRAINT FKeyapOrderBlocks2
FOREIGN KEY (rapOrderBlocks)
REFERENCES SQLUser.tapOrderBlocks(ID)
;
ALTER TABLE SQLUser.tapOrderSlides
ADD CONSTRAINT FKeyTests
FOREIGN KEY (rTests)
REFERENCES SQLUser.tTests(ID)
;
ALTER TABLE SQLUser.tapOrderSlides
ADD CONSTRAINT FKeyapOrderContainers2
FOREIGN KEY (rapOrderContainers)
REFERENCES SQLUser.tapOrderContainers(ID)
;
ALTER TABLE SQLUser.tapOrderSlides
ADD CONSTRAINT FKeyOrders2
FOREIGN KEY (rOrders)
REFERENCES SQLUser.tOrders(ID)
;


CREATE TABLE SQLUser.tapOrderObjects
(
   ID1 integer NOT NULL,
   Action varchar(50),
   AssociatedTo integer,
   CreationDate date,
   CreationTime time,
   ID integer PRIMARY KEY NOT NULL,
   Identifier varchar(50),
   Image varchar(2147483647),
   ObjectInfo varchar(2147483647),
   ObjectIterator integer,
   ObjectPiece integer,
   ObjectTotalPieces integer,
   ReportFovNote varchar(150),
   Status integer,
   Type varchar(50),
   URL varchar(32000),
   URLView varchar(32000),
   rOrders integer,
   rapOrderBlocks integer,
   rapOrderContainers integer,
   rapOrderSlides integer,
   TS_TSDateTime varchar(14),
   TS_TSUser varchar(16)
)
;
ALTER TABLE SQLUser.tapOrderObjects
ADD CONSTRAINT FKeyapOrderBlocks3
FOREIGN KEY (rapOrderBlocks)
REFERENCES SQLUser.tapOrderBlocks(ID)
;
ALTER TABLE SQLUser.tapOrderObjects
ADD CONSTRAINT FKeyapOrderSlides
FOREIGN KEY (rapOrderSlides)
REFERENCES SQLUser.tapOrderSlides(ID)
;
ALTER TABLE SQLUser.tapOrderObjects
ADD CONSTRAINT FKeyOrder
FOREIGN KEY (rOrders)
REFERENCES SQLUser.tOrders(ID)
;
ALTER TABLE SQLUser.tapOrderObjects
ADD CONSTRAINT FKeyapOrderContainers3
FOREIGN KEY (rapOrderContainers)
REFERENCES SQLUser.tapOrderContainers(ID)
;





INSERT INTO SQLUser.tTests (ID,InformIsolateds,KeepTest,MicroType,PrintLabel,Printable,ResultsGroup,ShowOrder,Status,TestAbbreviation,TestID,TestName,Use,TS_TSDateTime,TS_TSUser) VALUES (381,1,0,0,null,1,null,1,1,'HnE','999','HE Initial',14,'20230509041904','ROCHE');
INSERT INTO SQLUser.tTests (ID,InformIsolateds,KeepTest,MicroType,PrintLabel,Printable,ResultsGroup,ShowOrder,Status,TestAbbreviation,TestID,TestName,Use,TS_TSDateTime,TS_TSUser) VALUES (382,1,0,0,null,1,null,1,1,'HnE','HnE',null,14,'20230509042611','ROCHE_OC');
INSERT INTO SQLUser.tTests (ID,InformIsolateds,KeepTest,MicroType,PrintLabel,Printable,ResultsGroup,ShowOrder,Status,TestAbbreviation,TestID,TestName,Use,TS_TSDateTime,TS_TSUser) VALUES (384,1,0,0,null,1,null,1,1,'ICD3','ICD3',null,14,'20230509042620','ROCHE_OC');

INSERT INTO SQLUser.tPatients (ID,Age,DateOfBirth,ExtPatientID,FirstName,HistoryStatus,HostID,LastName,MiddleAndLastName,MiddleName,PatientID1,PatientID1Seq,PatientID2,PatientID3,Photo,PhotoName,Prefix,Race,Recycled,RecycledDateTime,Religion,SecondSurname,Sex,Suffix,SurNameAndName,TS_TSDateTime,TS_TSUser) VALUES (1,15834,{d '1980-01-01'},'0001','John','D',5,'Smithers','P Smithers','P','0001',null,null,null,null,null,null,null,0,null,null,null,'M','Mr','Smithers , John','20230509042759','SYS_HCA_5');
INSERT INTO SQLUser.tPatients (ID,Age,DateOfBirth,ExtPatientID,FirstName,HistoryStatus,HostID,LastName,MiddleAndLastName,MiddleName,PatientID1,PatientID1Seq,PatientID2,PatientID3,Photo,PhotoName,Prefix,Race,Recycled,RecycledDateTime,Religion,SecondSurname,Sex,Suffix,SurNameAndName,TS_TSDateTime,TS_TSUser) VALUES (2,15834,{d '1980-01-01'},'0001','John','D',5,'Smith3eb67f2ac6a','P Smith3eb67f2ac6a','P','D884B848-466F-43F8-B7E9-DDFBEC49EDB2',null,null,null,null,null,null,null,0,null,null,null,'M','Mr','Smith3eb67f2ac6a , John','20230509042807','SYS_HCA_5');
INSERT INTO SQLUser.tPatients (ID,Age,DateOfBirth,ExtPatientID,FirstName,HistoryStatus,HostID,LastName,MiddleAndLastName,MiddleName,PatientID1,PatientID1Seq,PatientID2,PatientID3,Photo,PhotoName,Prefix,Race,Recycled,RecycledDateTime,Religion,SecondSurname,Sex,Suffix,SurNameAndName,TS_TSDateTime,TS_TSUser) VALUES (3,15834,{d '1980-01-01'},'0001','John','D',5,'Smith','P Smith','P','F60E061B-FF53-4E2A-BD2A-F4256BB3825C',null,null,null,null,null,null,null,0,null,null,null,'M',null,'Smith , John','20230509042810','SYS_HCA_5');
INSERT INTO SQLUser.tPatients (ID,Age,DateOfBirth,ExtPatientID,FirstName,HistoryStatus,HostID,LastName,MiddleAndLastName,MiddleName,PatientID1,PatientID1Seq,PatientID2,PatientID3,Photo,PhotoName,Prefix,Race,Recycled,RecycledDateTime,Religion,SecondSurname,Sex,Suffix,SurNameAndName,TS_TSDateTime,TS_TSUser) VALUES (4,15834,{d '1980-01-01'},'MRN0468','Almaistairfdfd','D',5,'Fred','MiddleInitial Fred','MiddleInitial','MRN0468',null,null,null,null,null,null,null,0,null,null,null,'F','Suffix','Fred , Almaistairfdfd','20230509042841','SYS_HCA_5');

INSERT INTO SQLUser.tOrders (ID1,CaseTags,CloseDate,Destination,Diagnosis,Doctor,ExtPatientID,ExtRegisterDate,ExtRegisterHour,ExtSampleID,ExtractionDate,ExtractionHour,FirstEditDate,FirstEditHour,FirstEditPrinter,HasGraphic,HistoryStatus,HostCaptureID,HostID,ID,InProgress,InstrumentSampleID,LabSite,LabelPending,LastEditDate,LastEditHour,LastEditPrinter,Microbiology,Motive,Observations,OnHold,OrderStatus,Origin,PeriodUnits,PhysiologicalPeriod,PhysiologicalType,PreOrderStatus,Priority,Recycled,RecycledDateTime,RegisterDate,RegisterHour,RegisterYear,RegisteredOn,ReportNote,STAT,SampleID,SampleIDDate,SampleIDPrefix,SampleIDSeqNum,SampleIDSuffix,Service,Status,TypeExternalID,Use,VipOrderControl,VipOrderStatus,VisitNumber,WARetention,WARetentionCloseTS,WithDemographics,WorkFlow,lstScannedVRT,rPatients,TS_TSDateTime,TS_TSUser) VALUES (1,null,null,null,null,null,'0001',null,null,null,null,null,null,null,null,0,'D',null,5,1,0,null,null,1,null,null,null,1,null,null,0,1,null,null,null,null,null,null,0,null,{d '2023-05-09'},{t '04:27:58'},'2023',{d '2023-05-09'},null,'STAT','S21-0002',null,null,null,null,null,1,null,'14',null,null,null,1,null,1,null,'14,15',2,'20230509042826','SYS_HCA_5');
INSERT INTO SQLUser.tOrders (ID1,CaseTags,CloseDate,Destination,Diagnosis,Doctor,ExtPatientID,ExtRegisterDate,ExtRegisterHour,ExtSampleID,ExtractionDate,ExtractionHour,FirstEditDate,FirstEditHour,FirstEditPrinter,HasGraphic,HistoryStatus,HostCaptureID,HostID,ID,InProgress,InstrumentSampleID,LabSite,LabelPending,LastEditDate,LastEditHour,LastEditPrinter,Microbiology,Motive,Observations,OnHold,OrderStatus,Origin,PeriodUnits,PhysiologicalPeriod,PhysiologicalType,PreOrderStatus,Priority,Recycled,RecycledDateTime,RegisterDate,RegisterHour,RegisterYear,RegisteredOn,ReportNote,STAT,SampleID,SampleIDDate,SampleIDPrefix,SampleIDSeqNum,SampleIDSuffix,Service,Status,TypeExternalID,Use,VipOrderControl,VipOrderStatus,VisitNumber,WARetention,WARetentionCloseTS,WithDemographics,WorkFlow,lstScannedVRT,rPatients,TS_TSDateTime,TS_TSUser) VALUES (2,null,null,null,null,null,'0001',null,null,null,null,null,null,null,null,0,'D',null,5,2,0,null,null,1,null,null,null,1,null,null,0,1,null,null,null,null,null,null,0,null,{d '2023-05-09'},{t '04:28:10'},'2023',{d '2023-05-09'},null,'""','S22-0001',null,null,null,null,null,1,null,'14',null,null,null,1,null,1,null,'14,15',3,'20230509042810','SYS_HCA_5');
INSERT INTO SQLUser.tOrders (ID1,CaseTags,CloseDate,Destination,Diagnosis,Doctor,ExtPatientID,ExtRegisterDate,ExtRegisterHour,ExtSampleID,ExtractionDate,ExtractionHour,FirstEditDate,FirstEditHour,FirstEditPrinter,HasGraphic,HistoryStatus,HostCaptureID,HostID,ID,InProgress,InstrumentSampleID,LabSite,LabelPending,LastEditDate,LastEditHour,LastEditPrinter,Microbiology,Motive,Observations,OnHold,OrderStatus,Origin,PeriodUnits,PhysiologicalPeriod,PhysiologicalType,PreOrderStatus,Priority,Recycled,RecycledDateTime,RegisterDate,RegisterHour,RegisterYear,RegisteredOn,ReportNote,STAT,SampleID,SampleIDDate,SampleIDPrefix,SampleIDSeqNum,SampleIDSuffix,Service,Status,TypeExternalID,Use,VipOrderControl,VipOrderStatus,VisitNumber,WARetention,WARetentionCloseTS,WithDemographics,WorkFlow,lstScannedVRT,rPatients,TS_TSDateTime,TS_TSUser) VALUES (3,null,null,null,null,null,'MRN0468',null,null,null,null,null,null,null,null,0,'D',null,5,3,0,null,null,1,null,null,null,1,null,null,0,1,null,null,null,null,null,null,0,null,{d '2022-11-07'},{t '13:00:17'},'2022',{d '2023-05-09'},null,'""','Case19',null,null,null,null,null,1,null,'14',null,null,null,1,null,1,null,null,4,'20230509042837','SYS_HCA_5');

INSERT INTO SQLUser.tapOrderContainers (ID1,Assigned,CollectDate,CollectHour,ContainerID,ContainerStatus,ExternalID,HumanReadable,ID,OnHold,ReceivedDate,ReceivedHour,ReleaseDate,ReleaseTime,Sequence,rOrders,rapAnatomicSites,rapFacilities,rapSurgicalProcedure,rapTissues,TS_TSDateTime,TS_TSUser) VALUES (1,0,{d '2021-03-30'},{t '13:04:00'},'S21-0002;A',1,null,null,1,0,{d '2021-03-30'},{t '13:04:00'},null,null,'A',1,null,2,28,142,'20230509042836','SYS_HCA_15');
INSERT INTO SQLUser.tapOrderContainers (ID1,Assigned,CollectDate,CollectHour,ContainerID,ContainerStatus,ExternalID,HumanReadable,ID,OnHold,ReceivedDate,ReceivedHour,ReleaseDate,ReleaseTime,Sequence,rOrders,rapAnatomicSites,rapFacilities,rapSurgicalProcedure,rapTissues,TS_TSDateTime,TS_TSUser) VALUES (2,0,{d '2021-03-30'},{t '13:04:00'},'S21-0002;B',null,null,null,2,0,{d '2021-03-30'},{t '13:04:00'},null,null,'B',1,null,2,28,2,'20230509042807','SYS_HCA_5');
INSERT INTO SQLUser.tapOrderContainers (ID1,Assigned,CollectDate,CollectHour,ContainerID,ContainerStatus,ExternalID,HumanReadable,ID,OnHold,ReceivedDate,ReceivedHour,ReleaseDate,ReleaseTime,Sequence,rOrders,rapAnatomicSites,rapFacilities,rapSurgicalProcedure,rapTissues,TS_TSDateTime,TS_TSUser) VALUES (3,0,{d '2022-05-03'},{t '08:45:00'},'S22-0001;A',null,null,null,3,0,{d '2022-05-03'},{t '08:45:00'},null,null,'A',2,null,2,30,2,'20230509042810','SYS_HCA_5');
INSERT INTO SQLUser.tapOrderContainers (ID1,Assigned,CollectDate,CollectHour,ContainerID,ContainerStatus,ExternalID,HumanReadable,ID,OnHold,ReceivedDate,ReceivedHour,ReleaseDate,ReleaseTime,Sequence,rOrders,rapAnatomicSites,rapFacilities,rapSurgicalProcedure,rapTissues,TS_TSDateTime,TS_TSUser) VALUES (4,0,{d '2022-11-07'},{t '10:05:00'},'Case19;A',null,null,null,4,0,{d '2022-11-07'},{t '10:05:00'},null,null,'A',3,null,2,19,2,'20230509042841','SYS_HCA_5');

INSERT INTO SQLUser.tapOrderBlocks (ID1,Assigned,BlockID,ExternalID,HumanReadable,ID,OnHold,Printed,Sequence,StainingRequestComment,StainingRequestProtocol,Status,TissueSubtype,TissueSubtypeDesc,Virtual,rapOrderBlocks,rapOrderContainers,TS_TSDateTime,TS_TSUser) VALUES (1,0,'S21-0002;A;2',null,null,1,0,0,'2',null,null,1,'Left','Left Liver',0,null,1,'20230509042807','SYS_HCA_5');
INSERT INTO SQLUser.tapOrderBlocks (ID1,Assigned,BlockID,ExternalID,HumanReadable,ID,OnHold,Printed,Sequence,StainingRequestComment,StainingRequestProtocol,Status,TissueSubtype,TissueSubtypeDesc,Virtual,rapOrderBlocks,rapOrderContainers,TS_TSDateTime,TS_TSUser) VALUES (2,0,'S21-0002;B;3',null,null,2,0,0,'3',null,null,1,'Left','Left Liver',0,null,2,'20230509042807','SYS_HCA_5');
INSERT INTO SQLUser.tapOrderBlocks (ID1,Assigned,BlockID,ExternalID,HumanReadable,ID,OnHold,Printed,Sequence,StainingRequestComment,StainingRequestProtocol,Status,TissueSubtype,TissueSubtypeDesc,Virtual,rapOrderBlocks,rapOrderContainers,TS_TSDateTime,TS_TSUser) VALUES (3,0,'S21-0002;A;1',null,null,3,0,0,'1',null,null,1,'Left','Left Liver',0,null,1,'20230509042826','SYS_HCA_5');
INSERT INTO SQLUser.tapOrderBlocks (ID1,Assigned,BlockID,ExternalID,HumanReadable,ID,OnHold,Printed,Sequence,StainingRequestComment,StainingRequestProtocol,Status,TissueSubtype,TissueSubtypeDesc,Virtual,rapOrderBlocks,rapOrderContainers,TS_TSDateTime,TS_TSUser) VALUES (4,0,'S22-0001;A;1',null,null,4,0,0,'1',null,null,1,'Left','Left Liver',0,null,3,'20230509042810','SYS_HCA_5');
INSERT INTO SQLUser.tapOrderBlocks (ID1,Assigned,BlockID,ExternalID,HumanReadable,ID,OnHold,Printed,Sequence,StainingRequestComment,StainingRequestProtocol,Status,TissueSubtype,TissueSubtypeDesc,Virtual,rapOrderBlocks,rapOrderContainers,TS_TSDateTime,TS_TSUser) VALUES (5,0,'Case19;A;1',null,null,5,0,0,'1',null,null,1,'Left','Left Liver',0,null,4,'20230509042841','SYS_HCA_5');

INSERT INTO SQLUser.tapOrderSlides (ID1,ActionCode,AnalysisScoreData,Assigned,AutoPrinted,CloneType,ControlSlideType,ControlSlideTypeDescription,ExternalID,HostID,HostVersion,HumanReadable,ID,InProgress,Instrument,KeyCode,LabelPrinted,OnHold,OperationCode,PanelID,ProtocolIdentifier,ReagentInfo,ReagentLot,RecutText,ReportExcluded,ReportFovNote,ReportNote,Reprint,RescanComment,RescanReason,Rescanned,RunEstimatedTime,RunNumber,RunTime,ScoringType,Sequence,ShortName,SlideID,SlideLevel,SlidePosition,SlideScanStatus,SlideWSAStatus,StainEndDate,StainEndHour,StainStartDate,StainStartHour,StainerEffectiveType,StainerFriendlyName,StainerSerialNumber,StainingBatchID,Status,TemplateDisplayText,Unstained,Vendor,Virtual,algorithmId,keyParameterName,qualitativeResult,quantitativeResult,rOrders,rTests,rapOrderBlocks,rapOrderContainers,TS_TSDateTime,TS_TSUser) VALUES (1,'XO',null,0,null,null,null,null,null,null,null,null,1,0,null,null,null,0,null,null,'STAIN',null,null,null,null,null,null,null,null,null,null,null,null,null,null,'2',null,'S21-0002;A;2;2',null,null,null,null,null,null,null,null,null,null,null,null,1,null,0,null,0,null,null,null,null,null,381,1,null,'20230509042807','SYS_HCA_5');
INSERT INTO SQLUser.tapOrderSlides (ID1,ActionCode,AnalysisScoreData,Assigned,AutoPrinted,CloneType,ControlSlideType,ControlSlideTypeDescription,ExternalID,HostID,HostVersion,HumanReadable,ID,InProgress,Instrument,KeyCode,LabelPrinted,OnHold,OperationCode,PanelID,ProtocolIdentifier,ReagentInfo,ReagentLot,RecutText,ReportExcluded,ReportFovNote,ReportNote,Reprint,RescanComment,RescanReason,Rescanned,RunEstimatedTime,RunNumber,RunTime,ScoringType,Sequence,ShortName,SlideID,SlideLevel,SlidePosition,SlideScanStatus,SlideWSAStatus,StainEndDate,StainEndHour,StainStartDate,StainStartHour,StainerEffectiveType,StainerFriendlyName,StainerSerialNumber,StainingBatchID,Status,TemplateDisplayText,Unstained,Vendor,Virtual,algorithmId,keyParameterName,qualitativeResult,quantitativeResult,rOrders,rTests,rapOrderBlocks,rapOrderContainers,TS_TSDateTime,TS_TSUser) VALUES (2,'NW',null,0,null,null,null,null,null,null,null,null,2,1,null,null,null,0,null,null,'STAIN',null,null,null,null,null,null,null,null,null,null,null,null,null,null,'3',null,'S21-0002;A;2;3',null,null,'SCANNED',null,null,null,{d '2021-03-04'},{t '12:58:53'},'DAKO',null,'999999',null,3,null,0,null,0,null,null,null,null,null,382,1,null,'20230509042827','SYS_HCA_16');
INSERT INTO SQLUser.tapOrderSlides (ID1,ActionCode,AnalysisScoreData,Assigned,AutoPrinted,CloneType,ControlSlideType,ControlSlideTypeDescription,ExternalID,HostID,HostVersion,HumanReadable,ID,InProgress,Instrument,KeyCode,LabelPrinted,OnHold,OperationCode,PanelID,ProtocolIdentifier,ReagentInfo,ReagentLot,RecutText,ReportExcluded,ReportFovNote,ReportNote,Reprint,RescanComment,RescanReason,Rescanned,RunEstimatedTime,RunNumber,RunTime,ScoringType,Sequence,ShortName,SlideID,SlideLevel,SlidePosition,SlideScanStatus,SlideWSAStatus,StainEndDate,StainEndHour,StainStartDate,StainStartHour,StainerEffectiveType,StainerFriendlyName,StainerSerialNumber,StainingBatchID,Status,TemplateDisplayText,Unstained,Vendor,Virtual,algorithmId,keyParameterName,qualitativeResult,quantitativeResult,rOrders,rTests,rapOrderBlocks,rapOrderContainers,TS_TSDateTime,TS_TSUser) VALUES (3,'NW',null,0,null,null,null,null,null,null,null,null,3,0,null,null,null,1,null,null,'STAIN',null,null,null,null,null,null,null,null,null,null,null,null,null,null,'4',null,'S21-0002;B;3;4',null,null,null,null,null,null,null,null,null,null,null,null,1,null,0,null,0,null,null,null,null,null,381,2,null,'20230509042807','SYS_HCA_5');
INSERT INTO SQLUser.tapOrderSlides (ID1,ActionCode,AnalysisScoreData,Assigned,AutoPrinted,CloneType,ControlSlideType,ControlSlideTypeDescription,ExternalID,HostID,HostVersion,HumanReadable,ID,InProgress,Instrument,KeyCode,LabelPrinted,OnHold,OperationCode,PanelID,ProtocolIdentifier,ReagentInfo,ReagentLot,RecutText,ReportExcluded,ReportFovNote,ReportNote,Reprint,RescanComment,RescanReason,Rescanned,RunEstimatedTime,RunNumber,RunTime,ScoringType,Sequence,ShortName,SlideID,SlideLevel,SlidePosition,SlideScanStatus,SlideWSAStatus,StainEndDate,StainEndHour,StainStartDate,StainStartHour,StainerEffectiveType,StainerFriendlyName,StainerSerialNumber,StainingBatchID,Status,TemplateDisplayText,Unstained,Vendor,Virtual,algorithmId,keyParameterName,qualitativeResult,quantitativeResult,rOrders,rTests,rapOrderBlocks,rapOrderContainers,TS_TSDateTime,TS_TSUser) VALUES (4,'NW',null,0,null,null,null,null,null,null,null,null,4,0,null,null,null,0,null,null,'STAIN',null,null,null,null,null,null,null,null,null,null,null,null,null,null,'5',null,'S21-0002;B;3;5',null,null,null,null,null,null,null,null,null,null,null,null,1,null,0,null,0,null,null,null,null,null,381,2,null,'20230509042759','SYS_HCA_5');
INSERT INTO SQLUser.tapOrderSlides (ID1,ActionCode,AnalysisScoreData,Assigned,AutoPrinted,CloneType,ControlSlideType,ControlSlideTypeDescription,ExternalID,HostID,HostVersion,HumanReadable,ID,InProgress,Instrument,KeyCode,LabelPrinted,OnHold,OperationCode,PanelID,ProtocolIdentifier,ReagentInfo,ReagentLot,RecutText,ReportExcluded,ReportFovNote,ReportNote,Reprint,RescanComment,RescanReason,Rescanned,RunEstimatedTime,RunNumber,RunTime,ScoringType,Sequence,ShortName,SlideID,SlideLevel,SlidePosition,SlideScanStatus,SlideWSAStatus,StainEndDate,StainEndHour,StainStartDate,StainStartHour,StainerEffectiveType,StainerFriendlyName,StainerSerialNumber,StainingBatchID,Status,TemplateDisplayText,Unstained,Vendor,Virtual,algorithmId,keyParameterName,qualitativeResult,quantitativeResult,rOrders,rTests,rapOrderBlocks,rapOrderContainers,TS_TSDateTime,TS_TSUser) VALUES (5,'XO',null,0,null,null,null,null,null,null,null,null,5,1,null,null,null,0,null,null,'STAIN',null,null,null,null,null,null,null,null,null,null,null,null,null,null,'1',null,'S21-0002;A;1;1',null,null,null,null,null,null,{d '2021-03-04'},{t '12:58:53'},'DAKO',null,'999999',null,6,null,0,null,0,null,null,null,null,null,382,3,null,'20230509042836','SYS_HCA_15');
INSERT INTO SQLUser.tapOrderSlides (ID1,ActionCode,AnalysisScoreData,Assigned,AutoPrinted,CloneType,ControlSlideType,ControlSlideTypeDescription,ExternalID,HostID,HostVersion,HumanReadable,ID,InProgress,Instrument,KeyCode,LabelPrinted,OnHold,OperationCode,PanelID,ProtocolIdentifier,ReagentInfo,ReagentLot,RecutText,ReportExcluded,ReportFovNote,ReportNote,Reprint,RescanComment,RescanReason,Rescanned,RunEstimatedTime,RunNumber,RunTime,ScoringType,Sequence,ShortName,SlideID,SlideLevel,SlidePosition,SlideScanStatus,SlideWSAStatus,StainEndDate,StainEndHour,StainStartDate,StainStartHour,StainerEffectiveType,StainerFriendlyName,StainerSerialNumber,StainingBatchID,Status,TemplateDisplayText,Unstained,Vendor,Virtual,algorithmId,keyParameterName,qualitativeResult,quantitativeResult,rOrders,rTests,rapOrderBlocks,rapOrderContainers,TS_TSDateTime,TS_TSUser) VALUES (6,'NW',null,0,null,null,null,null,null,null,null,null,6,0,null,null,null,0,null,null,'STAIN',null,null,null,null,null,null,null,null,null,null,null,null,null,null,'1',null,'S22-0001;A;1;1',null,null,null,null,null,null,null,null,null,null,null,null,1,null,0,null,0,null,null,null,null,null,381,4,null,'20230509042810','SYS_HCA_5');
INSERT INTO SQLUser.tapOrderSlides (ID1,ActionCode,AnalysisScoreData,Assigned,AutoPrinted,CloneType,ControlSlideType,ControlSlideTypeDescription,ExternalID,HostID,HostVersion,HumanReadable,ID,InProgress,Instrument,KeyCode,LabelPrinted,OnHold,OperationCode,PanelID,ProtocolIdentifier,ReagentInfo,ReagentLot,RecutText,ReportExcluded,ReportFovNote,ReportNote,Reprint,RescanComment,RescanReason,Rescanned,RunEstimatedTime,RunNumber,RunTime,ScoringType,Sequence,ShortName,SlideID,SlideLevel,SlidePosition,SlideScanStatus,SlideWSAStatus,StainEndDate,StainEndHour,StainStartDate,StainStartHour,StainerEffectiveType,StainerFriendlyName,StainerSerialNumber,StainingBatchID,Status,TemplateDisplayText,Unstained,Vendor,Virtual,algorithmId,keyParameterName,qualitativeResult,quantitativeResult,rOrders,rTests,rapOrderBlocks,rapOrderContainers,TS_TSDateTime,TS_TSUser) VALUES (7,'NW',null,0,null,null,null,null,null,null,null,null,7,0,null,null,null,0,null,null,'STAIN',null,null,null,null,null,null,null,null,null,null,null,null,null,null,'2',null,'S22-0001;A;1;2',null,null,null,null,null,null,null,null,null,null,null,null,1,null,0,null,0,null,null,null,null,null,381,4,null,'20230509042810','SYS_HCA_5');
INSERT INTO SQLUser.tapOrderSlides (ID1,ActionCode,AnalysisScoreData,Assigned,AutoPrinted,CloneType,ControlSlideType,ControlSlideTypeDescription,ExternalID,HostID,HostVersion,HumanReadable,ID,InProgress,Instrument,KeyCode,LabelPrinted,OnHold,OperationCode,PanelID,ProtocolIdentifier,ReagentInfo,ReagentLot,RecutText,ReportExcluded,ReportFovNote,ReportNote,Reprint,RescanComment,RescanReason,Rescanned,RunEstimatedTime,RunNumber,RunTime,ScoringType,Sequence,ShortName,SlideID,SlideLevel,SlidePosition,SlideScanStatus,SlideWSAStatus,StainEndDate,StainEndHour,StainStartDate,StainStartHour,StainerEffectiveType,StainerFriendlyName,StainerSerialNumber,StainingBatchID,Status,TemplateDisplayText,Unstained,Vendor,Virtual,algorithmId,keyParameterName,qualitativeResult,quantitativeResult,rOrders,rTests,rapOrderBlocks,rapOrderContainers,TS_TSDateTime,TS_TSUser) VALUES (8,'NW',null,0,null,null,null,null,null,null,null,null,8,0,null,null,null,0,null,null,'STAIN',null,null,null,null,null,null,null,'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX','this is the reason of the rescan',1,null,null,null,null,'3',null,'S22-0001;A;1;3',null,null,null,null,null,null,null,null,null,null,null,null,1,null,0,null,0,null,null,null,null,null,381,4,null,'20230509042816','SYS_HCA_15');
INSERT INTO SQLUser.tapOrderSlides (ID1,ActionCode,AnalysisScoreData,Assigned,AutoPrinted,CloneType,ControlSlideType,ControlSlideTypeDescription,ExternalID,HostID,HostVersion,HumanReadable,ID,InProgress,Instrument,KeyCode,LabelPrinted,OnHold,OperationCode,PanelID,ProtocolIdentifier,ReagentInfo,ReagentLot,RecutText,ReportExcluded,ReportFovNote,ReportNote,Reprint,RescanComment,RescanReason,Rescanned,RunEstimatedTime,RunNumber,RunTime,ScoringType,Sequence,ShortName,SlideID,SlideLevel,SlidePosition,SlideScanStatus,SlideWSAStatus,StainEndDate,StainEndHour,StainStartDate,StainStartHour,StainerEffectiveType,StainerFriendlyName,StainerSerialNumber,StainingBatchID,Status,TemplateDisplayText,Unstained,Vendor,Virtual,algorithmId,keyParameterName,qualitativeResult,quantitativeResult,rOrders,rTests,rapOrderBlocks,rapOrderContainers,TS_TSDateTime,TS_TSUser) VALUES (9,'XO',null,0,null,null,null,null,null,null,null,null,9,0,null,null,null,0,null,null,'STAIN',null,null,null,null,null,null,null,null,null,null,null,null,null,null,'1',null,'Case19;A;1;1',null,null,null,null,null,null,null,null,null,null,null,null,1,null,0,null,0,null,null,null,null,null,381,5,null,'20230509042841','SYS_HCA_5');

INSERT INTO SQLUser.tapOrderObjects (ID1,Action,AssociatedTo,CreationDate,CreationTime,ID,Identifier,Image,ObjectInfo,ObjectIterator,ObjectPiece,ObjectTotalPieces,ReportFovNote,Status,Type,URL,URLView,rOrders,rapOrderBlocks,rapOrderContainers,rapOrderSlides,TS_TSDateTime,TS_TSUser) VALUES (1,'A',1,null,null,1,null,'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaQ==',null,null,null,null,null,null,'FV',null,null,1,null,null,null,'20230509042829','SYS_HCA_15');
INSERT INTO SQLUser.tapOrderObjects (ID1,Action,AssociatedTo,CreationDate,CreationTime,ID,Identifier,Image,ObjectInfo,ObjectIterator,ObjectPiece,ObjectTotalPieces,ReportFovNote,Status,Type,URL,URLView,rOrders,rapOrderBlocks,rapOrderContainers,rapOrderSlides,TS_TSDateTime,TS_TSUser) VALUES (2,'A',2,null,null,2,null,null,null,null,null,null,null,null,null,null,'?',null,null,1,null,'20230509042836','SYS_HCA_15');
INSERT INTO SQLUser.tapOrderObjects (ID1,Action,AssociatedTo,CreationDate,CreationTime,ID,Identifier,Image,ObjectInfo,ObjectIterator,ObjectPiece,ObjectTotalPieces,ReportFovNote,Status,Type,URL,URLView,rOrders,rapOrderBlocks,rapOrderContainers,rapOrderSlides,TS_TSDateTime,TS_TSUser) VALUES (3,'A',4,null,null,3,null,'12359087123957612561489046897785475245757903462452951289735127965127896578912634685123412423491290641523761235781234612789056290561804128905788=',null,null,null,null,null,null,'TN','?','?',null,null,null,5,'20230509042836','SYS_HCA_15');
