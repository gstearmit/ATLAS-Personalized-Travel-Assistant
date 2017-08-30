<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ns2:process xmlns:ns2="http://docs.oasis-open.org/wsbpel/2.0/process/executable">
    <ns2:sequence>
        <ns2:invoke>
            <name>TA_StartLegHandling</name>
            <order>0</order>
        </ns2:invoke>
        <ns2:pick xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="processActivity">
            <name>TA_IdentifyLeg</name>
            <order>1</order>
        </ns2:pick>
        <ns2:pick xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="processActivity">
            <name>TA_HandleLegResults</name>
            <order>2</order>
        </ns2:pick>
        <ns2:pick xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="processActivity">
            <name>TA_OrganizeData</name>
            <order>3</order>
        </ns2:pick>
        <ns2:pick xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="processActivity">
            <name>TA_ShowLegResults</name>
            <order>4</order>
        </ns2:pick>
        <ns2:invoke>
            <name>TA_EndLegHandling</name>
            <order>5</order>
        </ns2:invoke>
    </ns2:sequence>
</ns2:process>
