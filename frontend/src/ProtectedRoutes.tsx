import {Navigate, Outlet} from "react-router-dom";
import {MyUsersDto} from "./types/MyUsersDto.tsx";


type Props = {
    user: MyUsersDto|undefined|null
}
export default function ProtectedRoutes(props: Props) {

    const isAuthenticated = props.user !== undefined || true


    return (
        isAuthenticated ? <Outlet /> : <Navigate to="/" />
    )
}