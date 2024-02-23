import styled from 'styled-components';
import {NavLink} from "react-router-dom";
import {MyUsersDto} from "../types/MyUsersDto.tsx";



type NavBarProps = {
    logInUser: MyUsersDto;
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
                    <NavLink to="/user">Profil</NavLink>
                </>)}
            </NavContainer>
        </StyledNav>

    )
}
const StyledButton = styled.button`
    position: fixed;
    top: 3px;
    right: 3px;
    width: auto;
    display: flex;
    justify-content: center;
    border-radius: 12px;
    border: transparent none;
    background-color: transparent;
    padding: 8px 16px;
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
    font-size: 6vw;
    color: #FFF;
    font-family: Copperplate;
    font-style: normal;
    text-align: left;
    font-weight: 200;
    line-height: normal;
    background-color: RGB(129, 113, 87);
    text-decoration: none;
    border-radius: 0.375rem;
    padding: 0.5vw;
    cursor: pointer;

    &:hover {
        padding: 0;
        font-size: 6.5vw;
        margin: 0;
    }

`;

const StyledNav = styled.nav`
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    background-color: RGB(129, 113, 87);
    height: auto;
    width: 100vw;
    margin: 0;
    padding: 1vw;
    z-index: 1000000;
`;

const NavContainer = styled.div`
    max-width: 90vw;
    padding-left: 1rem;
    padding-right: 0.5vw;
    margin: 0;
    display: flex;
    justify-content: space-between;
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
    width: auto;
    height: auto;
    text-decoration: none;
    border-radius: 0.375rem;
    padding: 1vw;
    font-size: 1.7vw;
    font-weight: 500;
    margin: 0.5vw;
    cursor: pointer;

    &:hover {
        padding: 0.5vw 0 0.5vw 1.1vw;
        font-size: 2vw;
        margin: 0;
    }
`;
const NavLinkAdd = styled(NavLinks)`
    color: #ffffff;
    background-color: #1c859c;
    border-radius: 0.975rem;
    padding: 1vw 1.5vw;
    font-size: 1.7vw;
    font-weight: 500;
    margin: 0.5vw 1vw;
    cursor: pointer;

    &:hover {
        padding: 0.5vw 1vw;
        font-size: 2.3vw;
        margin: 0.55vw 0.5vw;
    }
`;
