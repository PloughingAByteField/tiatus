package org.tiatus.server.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.tiatus.entity.Club;
import org.tiatus.service.ClubServiceImpl;
import org.tiatus.service.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by johnreynolds on 09/07/2016.
 */
@ExtendWith(MockitoExtension.class)
public class ClubRestPointTest extends RestTestBase {

    private ClubRestPoint clubRestPoint = new ClubRestPoint();

    @Mock
    private HttpSession httpSessionMock;

    @Mock 
    private Request requestMock;

    @Mock
    private HttpServletRequest httpServletRequestMock;

    @Mock
    private ClubServiceImpl clubServiceMock;

    @Mock
    private UriInfo uriInfoMock;
    
    @BeforeEach
    public void setup() throws Exception {
        clubRestPoint.setService(clubServiceMock);
    }


    @Test
    public void addClub() throws Exception {
        when(httpServletRequestMock.getSession()).thenReturn(httpSessionMock);
        when(clubServiceMock.addClub(any(), any())).thenReturn(new Club());
        Response response = clubRestPoint.addClub(uriInfoMock, httpServletRequestMock, new Club());

        Assertions.assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
    }

    @Test
    public void addClubServiceException() throws Exception {
        when(httpServletRequestMock.getSession()).thenReturn(httpSessionMock);
        
        doThrow(ServiceException.class).when(clubServiceMock).addClub(any(), any());
        Response response = clubRestPoint.addClub(uriInfoMock, httpServletRequestMock, new Club());

        Assertions.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void deleteClub() throws Exception {
        when(httpServletRequestMock.getSession()).thenReturn(httpSessionMock);
        when(clubServiceMock.getClubForId(any())).thenReturn(new Club());
        doNothing().when(clubServiceMock).deleteClub(any(), any());
        Response response = clubRestPoint.removeClub("1", httpServletRequestMock);

        Assertions.assertEquals(HttpServletResponse.SC_NO_CONTENT, response.getStatus());
    }

    @Test
    public void deleteClubServiceException() throws Exception {
        when(httpServletRequestMock.getSession()).thenReturn(httpSessionMock);
        when(clubServiceMock.getClubForId(any())).thenReturn(new Club());
        doThrow(ServiceException.class).when(clubServiceMock).deleteClub(any(), any());
        Response response = clubRestPoint.removeClub("1", httpServletRequestMock);

        Assertions.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void updateClub() throws Exception {
        when(httpServletRequestMock.getSession()).thenReturn(httpSessionMock);
        when(clubServiceMock.getClubForId(any())).thenReturn(new Club());
        doNothing().when(clubServiceMock).updateClub(any(), any());
        Response response = clubRestPoint.updateClub("1", httpServletRequestMock, new Club());

        Assertions.assertEquals(HttpServletResponse.SC_NO_CONTENT, response.getStatus());
    }

    @Test
    public void updateClubServiceException() throws Exception {
        when(httpServletRequestMock.getSession()).thenReturn(httpSessionMock);
        when(clubServiceMock.getClubForId(any())).thenReturn(new Club());
        doThrow(ServiceException.class).when(clubServiceMock).updateClub(any(), any());
        Response response = clubRestPoint.updateClub("1", httpServletRequestMock, new Club());

        Assertions.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }


    @Test
    public void getClubs() throws Exception {
        List<Club> clubs = new ArrayList<>();
        Club club = new Club();
        club.setId(1L);
        club.setClubName("Club 1");
        clubs.add(club);
        when(clubServiceMock.getClubs()).thenReturn(clubs);
        Response response = clubRestPoint.getClubs(requestMock);

        Assertions.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    @Test 
    @Disabled
    public void checkGetClubAnnotations() throws Exception {
    //     EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "clubs", "GET");
    //     if (endPointDetail == null) {
    //         System.out.println("Failed to find end point for GET:club");
    //         throw new Exception();
    //     }

    //     if (!EndPointDetail.isValid(endPointDetail)) {
    //         System.out.println("End point for GET:club is not valid");
    //         throw new Exception();
    //     }

    //     if (!endPointDetail.getMethodName().equals("getClubs")) {
    //         System.out.println("End point method name has changed");
    //         throw new Exception();
    //     }

    //     if (endPointDetail.isAllowAll() == null || !endPointDetail.isAllowAll()) {
    //         System.out.println("End point is not allowed all");
    //         throw new Exception();
    //     }
    }

    @Test
    @Disabled
    public void checkAddClubAnnotations() throws Exception {
    //     EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "clubs", "POST");
    //     if (endPointDetail == null) {
    //         System.out.println("Failed to find end point for POST:club");
    //         throw new Exception();
    //     }

    //     if (!EndPointDetail.isValid(endPointDetail)) {
    //         System.out.println("End point for POST:club is not valid");
    //         throw new Exception();
    //     }

    //     if (!endPointDetail.getMethodName().equals("addClub")) {
    //         System.out.println("End point method name has changed");
    //         throw new Exception();
    //     }

    //     if (!endPointDetail.getRolesAllowed().contains(Role.ADMIN)) {
    //         System.out.println("End point does not have expected roles");
    //         throw new Exception();
    //     }
    }

    @Test
    @Disabled
    public void checkDeleteClubAnnotations() throws Exception {
    //     EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "clubs/{id}", "DELETE");
    //     if (endPointDetail == null) {
    //         System.out.println("Failed to find end point for DELETE:club");
    //         throw new Exception();
    //     }

    //     if (!EndPointDetail.isValid(endPointDetail)) {
    //         System.out.println("End point for DELETE:club is not valid");
    //         throw new Exception();
    //     }

    //     if (!endPointDetail.getMethodName().equals("removeClub")) {
    //         System.out.println("End point method name has changed");
    //         throw new Exception();
    //     }

    //     if (!endPointDetail.getRolesAllowed().contains(Role.ADMIN)) {
    //         System.out.println("End point does not have expected roles");
    //         throw new Exception();
    //     }
    }

    @Test
    @Disabled
    public void checkUpdateClubAnnotations() throws Exception {
    //     EndPointDetail endPointDetail = getEndPointDetail(endPointDetails, "clubs/{id}", "PUT");
    //     if (endPointDetail == null) {
    //         System.out.println("Failed to find end point for PUT:club");
    //         throw new Exception();
    //     }

    //     if (!EndPointDetail.isValid(endPointDetail)) {
    //         System.out.println("End point for PUT:club is not valid");
    //         throw new Exception();
    //     }

    //     if (!endPointDetail.getMethodName().equals("updateClub")) {
    //         System.out.println("End point method name has changed");
    //         throw new Exception();
    //     }

    //     if (!endPointDetail.getRolesAllowed().contains(Role.ADMIN)) {
    //         System.out.println("End point does not have expected roles");
    //         throw new Exception();
    //     }
    }
}
