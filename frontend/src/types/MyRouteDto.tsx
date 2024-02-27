import {MyCoords} from "./MyCoords.tsx";
import {MyUsersDto} from "./MyUsersDto.tsx";

export type MyRouteDto = {
    coords: MyCoords[],
    members: MyUsersDto[];
    name: string,
    dateTime: Date
}