package org.cccs.tfs.integration.oxm

import org.junit.Test
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.oxm.xstream.XStreamMarshaller
import org.springframework.beans.factory.annotation.Autowired
import org.cccs.tfs.finder.PrincipalFinder

import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource
import org.cccs.tfs.domain.Principal
import org.cccs.tfs.domain.Location;

@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
class TestPrincipalConverter extends XMLTestCase {

    static {
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
    }

    @Autowired
    private PrincipalFinder finder;

    @Autowired
    private XStreamMarshaller marshaller;

    String expectedAll = """
        <resources>
            <principal id="1">
                <id>1</id>
                <shortName>BoyCook</shortName>
                <password>password</password>
                <foreName>Boy</foreName>
                <surName>Cook</surName>
                <phoneNumber>07918880501</phoneNumber>
                <email>boycook@me.com</email>
                <friends/>
                <friendRequests/>
            </principal>
            <principal id="2">
                <id>2</id>
                <shortName>CraigCook</shortName>
                <password>password</password>
                <foreName>Craig</foreName>
                <surName>Cook</surName>
                <phoneNumber>07918880501</phoneNumber>
                <email>craig.cook@bt.com</email>
                <friends/>
                <friendRequests/>
            </principal>
        </resources>
    """

    String expectedBoyCook = """
        <principal id="1">
            <id>1</id>
            <shortName>BoyCook</shortName>
            <password>password</password>
            <foreName>Boy</foreName>
            <surName>Cook</surName>
            <phoneNumber>07918880501</phoneNumber>
            <email>boycook@me.com</email>
            <friends/>
            <friendRequests/>
        </principal>
    """

    String expectedBoyCookWithLocation = """
        <principal id="1">
            <id>1</id>
            <shortName>BoyCook</shortName>
            <password>password</password>
            <foreName>Boy</foreName>
            <surName>Cook</surName>
            <phoneNumber>07918880501</phoneNumber>
            <email>boycook@me.com</email>
            <location>
                <latitude>123.0</latitude>
                <longitude>456.0</longitude>
            </location>
            <friends/>
            <friendRequests/>
        </principal>
    """

    String expectedBoyCookWithFriend = """
        <principal id="1">
            <id>1</id>
            <shortName>BoyCook</shortName>
            <password>password</password>
            <foreName>Boy</foreName>
            <surName>Cook</surName>
            <friends>
                <principal id="3">
                    <id>3</id>
                    <shortName>FooBar</shortName>
                    <password>password</password>
                    <foreName>Foo</foreName>
                    <surName>Bar</surName>
                    <phoneNumber>07918880501</phoneNumber>
                    <email>craig.cook@bt.com</email>
                </principal>
                <principal id="2">
                    <id>2</id>
                    <shortName>CraigCook</shortName>
                    <password>password</password>
                    <foreName>Craig</foreName>
                    <surName>Cook</surName>
                    <phoneNumber>07918880501</phoneNumber>
                    <email>craig.cook@bt.com</email>
                </principal>
            </friends>
            <friendRequests/>
            <phoneNumber>07918880501</phoneNumber>
            <email>boycook@me.com</email>
        </principal>
    """

    String expectedBoyCookWithFriendRequest = """
        <principal id="1">
            <id>1</id>
            <shortName>BoyCook</shortName>
            <password>password</password>
            <foreName>Boy</foreName>
            <surName>Cook</surName>
            <friendRequests>
                <principal id="3">
                    <id>3</id>
                    <shortName>FooBar</shortName>
                    <password>password</password>
                    <foreName>Foo</foreName>
                    <surName>Bar</surName>
                    <phoneNumber>07918880501</phoneNumber>
                    <email>craig.cook@bt.com</email>
                </principal>
            </friendRequests>
            <friends>
                <principal id="2">
                    <id>2</id>
                    <shortName>CraigCook</shortName>
                    <password>password</password>
                    <foreName>Craig</foreName>
                    <surName>Cook</surName>
                    <phoneNumber>07918880501</phoneNumber>
                    <email>craig.cook@bt.com</email>
                </principal>
            </friends>
            <phoneNumber>07918880501</phoneNumber>
            <email>boycook@me.com</email>
        </principal>
    """

    String expectedBoyCookWithFriendsWithDistance = """
        <principal id="1">
            <id>1</id>
            <shortName>BoyCook</shortName>
            <password>password</password>
            <foreName>Boy</foreName>
            <surName>Cook</surName>
            <friends>
                <principal id="3">
                    <id>3</id>
                    <shortName>FooBar</shortName>
                    <password>password</password>
                    <foreName>Foo</foreName>
                    <surName>Bar</surName>
                    <phoneNumber>07918880501</phoneNumber>
                    <email>craig.cook@bt.com</email>
                    <distance>10.0</distance>
                </principal>
                <principal id="2">
                    <id>2</id>
                    <shortName>CraigCook</shortName>
                    <password>password</password>
                    <foreName>Craig</foreName>
                    <surName>Cook</surName>
                    <phoneNumber>07918880501</phoneNumber>
                    <email>craig.cook@bt.com</email>
                    <distance>20.0</distance>
                </principal>
            </friends>
            <friendRequests/>
            <phoneNumber>07918880501</phoneNumber>
            <email>boycook@me.com</email>
        </principal>
    """

    @Test
    public void marshallingListShouldWork() throws IOException {
        String marshalled = marshal(finder.search("cook"));
        println marshalled;
        assertXMLEqual("Comparing marshalled xml to expected xml", expectedAll, marshalled);
    }

    @Test
    public void marshallingAPersonShouldWork() throws IOException {
        String marshalled = marshal(finder.find("BoyCook"));
        println marshalled;
        assertXMLEqual("Comparing marshalled xml to expected xml", expectedBoyCook, marshalled);
    }

    @Test
    public void marshallingAPersonWithLocationShouldWork() throws IOException {
        def boycook = finder.find("BoyCook")
        boycook.location = new Location(123, 456)
        String marshalled = marshal(boycook);
        println marshalled;
        assertXMLEqual("Comparing marshalled xml to expected xml", expectedBoyCookWithLocation, marshalled);
    }

    @Test
    public void marshallingAPersonWithFriendsShouldWork() throws IOException {
        def boyCook = finder.find("BoyCook")
        def craigCook = finder.find("CraigCook")
        def fooBar = finder.find("FooBar")
        boyCook.addFriend(craigCook)
        boyCook.addFriend(fooBar)
        String marshalled = marshal(boyCook);
        println marshalled;
        assertXMLEqual("Comparing marshalled xml to expected xml", expectedBoyCookWithFriend, marshalled);
    }

    @Test
    public void marshallingAPersonWithFriendsAndDistanceShouldWork() throws IOException {
        def boyCook = finder.find("BoyCook")
        def craigCook = finder.find("CraigCook")
        def fooBar = finder.find("FooBar")
        fooBar.distance = 10
        craigCook.distance = 20
        boyCook.addFriend(craigCook)
        boyCook.addFriend(fooBar)
        String marshalled = marshal(boyCook);
        println marshalled;
        assertXMLEqual("Comparing marshalled xml to expected xml", expectedBoyCookWithFriendsWithDistance, marshalled);
    }

    @Test
    public void marshallingAPersonWithFriendsAndFriendRequestsShouldWork() throws IOException {
        def boyCook = finder.find("BoyCook")
        def craigCook = finder.find("CraigCook")
        def fooBar = finder.find("FooBar")
        boyCook.addFriend(craigCook)
        boyCook.addFriendRequest(fooBar)
        String marshalled = marshal(boyCook);
        println marshalled;
        assertXMLEqual("Comparing marshalled xml to expected xml", expectedBoyCookWithFriendRequest, marshalled);
    }

    @Test
    public void unmarshallingAPersonShouldWork() throws IOException {
        unmarshalBoyCook expectedBoyCook
    }

    @Test
    public void unmarshallingAPersonWithLocationShouldWork() throws IOException {
        Principal p = unmarshalBoyCook(expectedBoyCookWithLocation)

        assertEquals p.location.latitude(), "123.0"
        assertEquals p.location.longitude(), "456.0"
    }

    @Test
    public void unmarshallingAPersonWithFriendsShouldWork() throws IOException {
        Principal p = unmarshalBoyCook(expectedBoyCookWithFriend)
        assertEquals 2, p.friends.size()
    }

    @Test
    public void unmarshallingAPersonWithFriendsAndFriendRequestsShouldWork() throws IOException {
        Principal p = unmarshalBoyCook(expectedBoyCookWithFriendRequest)
        assertEquals 1, p.friends.size()
        assertEquals 1, p.friendRequests.size()
    }

    private Principal unmarshalBoyCook(String xml) {
        Principal p = unmarshal(xml)

        assertEquals 1, p.id
        assertEquals 'BoyCook', p.shortName
        assertEquals 'Boy', p.foreName
        assertEquals 'Cook', p.surName
        assertEquals '07918880501', p.phoneNumber
        assertEquals 'boycook@me.com', p.email

        return p
    }

    public static String strip(String xml) {
        String removedIds = xml.replaceAll("id=\".+?\"", "id=\"0\"");
        return removedIds.replaceAll("<id>.*</id>", "<id>0</id>");
    }

    private String marshal(final Object obj) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        marshal(obj, new BufferedOutputStream(bos));
        return bos.toString();
    }

    private void marshal(final Object obj, final OutputStream stream) throws IOException {
        final Result result = new StreamResult(stream);
        marshaller.marshal(obj, result);
    }

    public Object unmarshal(final String data) throws IOException {
        return unmarshal(new ByteArrayInputStream(data.getBytes()));
    }

    public Object unmarshal(final InputStream stream) throws IOException {
        return marshaller.unmarshal(new StreamSource(stream));
    }
}
