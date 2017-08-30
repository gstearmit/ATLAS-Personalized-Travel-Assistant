<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ns2:process xmlns:ns2="http://docs.oasis-open.org/wsbpel/2.0/process/executable">
    <ns2:sequence>
        <ns2:invoke>
            <name>TA_StartInteraction</name>
            <order>0</order>
        </ns2:invoke>
        <ns2:invoke xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="processActivity">
            <name>TA_StartChatbot</name>
            <order>1</order>
        </ns2:invoke>
        <ns2:invoke xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="processActivity">
            <name>TA_UseCurrentLocation</name>
            <order>2</order>
        </ns2:invoke>
        <ns2:invoke xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="processActivity">
            <name>TA_InsertSource</name>
            <order>3</order>
        </ns2:invoke>
        <ns2:invoke xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="processActivity">
            <name>TA_InsertDestination</name>
            <order>4</order>
        </ns2:invoke>
        <ns2:invoke xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="processActivity">
            <name>TA_InsertOptionalData</name>
            <order>5</order>
        </ns2:invoke>
        <ns2:invoke>
            <name>TA_PlanRequest</name>
            <order>6</order>
        </ns2:invoke>
        <ns2:reply>
            <name>TA_PlanResponse</name>
            <order>7</order>
        </ns2:reply>
        <ns2:invoke xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="processActivity">
            <name>TA_DefineDataViewerPattern</name>
            <order>8</order>
        </ns2:invoke>
        <ns2:invoke xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="processActivity">
            <name>TA_ShowResults</name>
            <order>9</order>
        </ns2:invoke>
        <ns2:invoke xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="processActivity">
            <name>TA_ChooseAlternative</name>
            <order>10</order>
        </ns2:invoke>
        <ns2:invoke>
            <name>TA_ProvideChoice</name>
            <order>11</order>
        </ns2:invoke>
        <ns2:invoke xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="processActivity">
            <name>TA_DefineJourneyLegs</name>
            <order>12</order>
        </ns2:invoke>
        <ns2:invoke xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="processActivity">
            <name>TA_AssistantPlanningComplete</name>
            <order>13</order>
        </ns2:invoke>
    </ns2:sequence>
</ns2:process>
