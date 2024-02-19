import {useEffect, useState} from "react";
import "../index.css";
import {AnimatePresence, motion} from "framer-motion";
import styled from "styled-components";
import {MyImages} from "../types/MyImages.tsx";
import axios from "axios";

type Props={
    routeId:string| undefined;
}
export default function Carousel(props:Props) {
    const [currentIndex, setCurrentIndex] = useState(0);
    const [direction, setDirection] = useState("");
    const [images, setImages] = useState<MyImages[]>([])
    useEffect(() => {
        axios.get("/api/images").then(response =>
            setImages(response.data))
    }, [])

    const imgUrls: string[] = images?.filter((img: MyImages) => img.routeId === props.routeId)
        .map((img: MyImages) => img.url);

    const slideVariants = {
        hiddenRight: {
            x: "100%",
            opacity: 0,
        },
        hiddenLeft: {
            x: "-100%",
            opacity: 0,
        },
        visible: {
            x: "0",
            opacity: 1,
            transition: {
                duration: 1,
            },
        },
        exit: {
            opacity: 0,
            scale: 0.8,
            transition: {
                duration: 0.5,
            },
        },
    };
    const slidersVariants = {
        hover: {
            scale: 1.2,
            backgroundColor: "#ff00008e",
        },
    };
    const dotsVariants = {
        initial: {
            y: 0,
        },
        animate: {
            y: -10,
            scale: 1.2,
            transition: {type: "spring", stiffness: 1000, damping: "10"},
        },
        hover: {
            scale: 1.1,
            transition: {duration: 0.2},
        },
    };

    const handleNext = () => {
        setDirection("right");
        setCurrentIndex((prevIndex) =>
            prevIndex + 1 === imgUrls.length ? 0 : prevIndex + 1
        );
    };

    const handlePrevious = () => {
        setDirection("left");

        setCurrentIndex((prevIndex) =>
            prevIndex - 1 < 0 ? imgUrls.length - 1 : prevIndex - 1
        );
    };

    const handleDotClick = (index:number) => {
        setDirection(index > currentIndex ? "right" : "left");
        setCurrentIndex(index);
    };

    return (
        <StyledCarousel className="carousel">
            <div className="carousel-images">
                <AnimatePresence>
                    <motion.img
                        key={currentIndex}
                        src={imgUrls[currentIndex]}
                        initial={direction === "right" ? "hiddenRight" : "hiddenLeft"}
                        animate="visible"
                        exit="exit"
                        variants={slideVariants}
                    />
                </AnimatePresence>
                <div className="slide_direction">
                    <motion.div
                        variants={slidersVariants}
                        whileHover="hover"
                        className="left"
                        onClick={handlePrevious}
                    >
                        <svg
                            xmlns="http://www.w3.org/2000/svg"
                            height="20"
                            viewBox="0 96 960 960"
                            width="20"
                        >
                            <path d="M400 976 0 576l400-400 56 57-343 343 343 343-56 57Z" />
                        </svg>
                    </motion.div>
                    <motion.div
                        variants={slidersVariants}
                        whileHover="hover"
                        className="right"
                        onClick={handleNext}
                    >
                        <svg
                            xmlns="http://www.w3.org/2000/svg"
                            height="20"
                            viewBox="0 96 960 960"
                            width="20"
                        >
                            <path d="m304 974-56-57 343-343-343-343 56-57 400 400-400 400Z" />
                        </svg>
                    </motion.div>
                </div>
            </div>
            <div className="carousel-indicator">
                {imgUrls.map((_, index) => (
                    <motion.div
                        key={_}
                        className={`dot ${currentIndex === index ? "active" : ""}`}
                        onClick={() => handleDotClick(index)}
                        initial="initial"
                        animate={currentIndex === index ? "animate" : ""}
                        whileHover="hover"
                        variants={dotsVariants}
                    ></motion.div>
                ))}
            </div>
        </StyledCarousel>
    );

}
const StyledCarousel = styled.div`
    display: ${( imgUrls) => (!imgUrls ? "none" : "block")} !important;
`;