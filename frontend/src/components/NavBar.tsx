import styled from 'styled-components';
import {NavLink} from "react-router-dom";
import {MyUsersDto} from "../types/MyUsersDto.tsx";

type NavBarProps = {
    logInUser: MyUsersDto | null;
    logout: () => void;
};

export default function NavBar(props: NavBarProps) {

    function login() {
        const host =
            window.location.host === "localhost:5173"
                ? "http://localhost:8080"
                : window.location.origin;

        window.open(host + "/oauth2/authorization/google", "_self");
    }

    return (

        <StyledNav>
            <NavContainer>
                <NavLinkHeading to="/">Pfad Keeper</NavLinkHeading>
                <FlexContainer>

                    <NavLinkAdd to="/routes/add"> + New Route</NavLinkAdd>
                    <NavLinks to="/routes">Meine Routen</NavLinks>

                </FlexContainer>
<LogInDiv>
                {!props.logInUser && (
                    <StyledButton
                        onClick={login}
                    >
                        Login
                    </StyledButton>
                )}
                {!!props.logInUser && (<>
                    <StyledButton
                        onClick={props.logout}
                    >
                        Logout
                    </StyledButton>
                    <NavLinkProfil to="/user">
                        <StyledSvg
                            width="18" height="18" viewBox="0 0 30 30" fill="none" xmlns="http://www.w3.org/2000/svg"
                        >
                            <path
                                d="M18 20C18 18.4087 17.3679 16.8826 16.2426 15.7574C15.1174 14.6321 13.5913 14 12 14C10.4087 14 8.88258 14.6321 7.75736 15.7574C6.63214 16.8826 6 18.4087 6 20"
                                fill="#01A3C6"/>
                            <path
                                d="M18 20C18 18.4087 17.3679 16.8826 16.2426 15.7574C15.1174 14.6321 13.5913 14 12 14C10.4087 14 8.88258 14.6321 7.75736 15.7574C6.63214 16.8826 6 18.4087 6 20"
                                stroke="white" strokeLinecap="round" strokeLinejoin="round"/>
                            <path
                                d="M15.5 10C15.5 11.933 13.933 13.5 12 13.5C10.067 13.5 8.5 11.933 8.5 10C8.5 8.067 10.067 6.5 12 6.5C13.933 6.5 15.5 8.067 15.5 10Z"
                                fill="#01A3C6" stroke="white" strokeLinecap="round" strokeLinejoin="round"/>
                            <path
                                d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z"
                                stroke="white" strokeLinecap="round" strokeLinejoin="round"/>
                        </StyledSvg>
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">

                        </svg>
                    </NavLinkProfil>
                </>)} </LogInDiv>
            </NavContainer>
        </StyledNav>

    )
}
const LogInDiv =styled.div`
    width:10vw;
    height: 5.8vh;
    display: block;
    position: relative;
    
`;

const StyledSvg = styled.svg`
  &:hover {
    width: 20px;
    height: 20px;
  }
`;
const NavLinkProfil = styled(NavLink)`
    background-color: transparent;
    width:100%;
    position: relative;
    top: 1rem;
    right: 0;
    display: flex;
    justify-content: center;
    text-decoration: none;
    cursor: pointer;
`;
const StyledButton = styled.button`
    position: fixed;
    top: 2px;
    right: 2vw;
    width: auto;
    border: transparent none;
    background-color: transparent;
    padding: 8px 16px;
    font-size: 1vw;
    font-weight: 300;
    color: white;

    &:hover {
        color: #01a3c6; /* Textfarbe beim Hover-Zustand ändern */
    }

    &:focus-visible {
        outline: none;
        outline-offset: 2px;
        box-shadow: 0 0 0 3px rgba(66, 153, 225, 0.5); /* Hervorhebungsfarbe ändern */
    }
`;

const NavLinkHeading = styled(NavLink)`
    font-size: 1.3rem;
    color: #FFF;
    font-family: Copperplate;
    font-style: normal;
    text-align: left;
    font-weight: 200;
    line-height: normal;
    background-color: transparent;
    text-decoration: none;
    border-radius: 0.375rem;
    margin-right: 9vw;
    padding: 0.5vw;
    cursor: pointer;

    &:hover {
        padding: 0;
        font-size: 1.4rem;
        margin-right: 8.5vw;
    }

`;

const StyledNav = styled.nav`
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    background-color: RGB(129, 113, 87);
    max-height: 6vh;
    width: 100%;
    margin: 0;
    padding: 1vw;
    z-index: 1000000;
`;

const NavContainer = styled.div`
    max-width: 98vw;
    max-height: 6vh;
    padding-left: 1rem;
    margin: 0;
    display: flex;
    justify-content: space-between;
    align-items: center;
`;

const FlexContainer = styled.div`
    display: flex;
    flex-wrap: wrap;
    padding: 0.75rem 0 0.75rem 0;
    max-width: 60vw;
    align-items: center;
    justify-content: space-around;
`;


const NavLinks = styled(NavLink)`
    color: #ffffff;
    background-color: RGB(129, 113, 87);
    text-decoration: none;
    border-radius: 0.375rem;
    padding: 0.5vw 1.5vw;
    font-size: 1.7vw;
    font-weight: 500;
    margin: 0.5vw;
    cursor: pointer;

    &:hover {
        padding: 0 1.10vw;
        font-size: 2vw;
        margin: 0.5vw 0 0.5vw 0;
    }
`;
const NavLinkAdd = styled(NavLinks)`
    color: #ffffff;
    background-color: #1c859c;
    border-radius: 0.975rem;
    padding: 0.5vw 1.5vw;
    font-size: 1.7vw;
    font-weight: 500;
    margin: 0.5vw 1vw;
    cursor: pointer;

    &:hover {
        padding: 0 1.25vw;
        font-size: 2vw;
        margin: 0.55vw 0.5vw;
    }
`;
