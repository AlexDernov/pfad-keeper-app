import RouteForm from "./RouteForm.tsx";
import {MyRouteDto} from "../types/MyRouteDto.tsx";
import {MyUser} from "../types/MyUsers.tsx";


type PropsNewRoute = {
    onSubmit: (route: MyRouteDto) => void;
    hostUser:MyUser;
}
export default function NewRoute(props: PropsNewRoute) {
    return (
            <RouteForm onSubmit={props.onSubmit} name={""} date={""} isEdit={false} coords={[]} hostUser={props.hostUser} routeId={""} usersOfRoute={[]} allUsers={[]}/>
    )
}
