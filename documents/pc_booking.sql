-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Ott 13, 2019 alle 18:04
-- Versione del server: 5.6.20
-- PHP Version: 5.5.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `pc_booking`
--
CREATE DATABASE IF NOT EXISTS `pc_booking` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `pc_booking`;

-- --------------------------------------------------------

--
-- Struttura della tabella `booking`
--

CREATE TABLE IF NOT EXISTS `booking` (
  `Username` varchar(255) NOT NULL,
  `StartTime` time NOT NULL,
  `Date` date NOT NULL,
  `PCNumber` int(11) NOT NULL,
  `RoomName` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `booking`
--

INSERT INTO `booking` (`Username`, `StartTime`, `Date`, `PCNumber`, `RoomName`) VALUES
('m.pettorali', '10:30:00', '2019-10-12', 1, 'S.I.2'),
('r.nocerino', '12:30:00', '2019-10-14', 1, 'S.I.1'),
('r.xefraj', '09:30:00', '2019-10-12', 1, 'S.I.3'),
('d.lorenzoni2', '10:30:00', '2019-10-12', 2, 'S.I.1'),
('r.nocerino', '10:30:00', '2019-10-12', 3, 'S.I.1');

-- --------------------------------------------------------

--
-- Struttura della tabella `pc`
--

CREATE TABLE IF NOT EXISTS `pc` (
  `PCNumber` int(11) NOT NULL,
  `RoomName` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `pc`
--

INSERT INTO `pc` (`PCNumber`, `RoomName`) VALUES
(1, 'S.I.1'),
(2, 'S.I.1'),
(3, 'S.I.1'),
(4, 'S.I.1'),
(1, 'S.I.2'),
(2, 'S.I.2'),
(3, 'S.I.2'),
(4, 'S.I.2'),
(1, 'S.I.3'),
(2, 'S.I.3'),
(3, 'S.I.3'),
(4, 'S.I.3'),
(5, 'S.I.3');

-- --------------------------------------------------------

--
-- Struttura della tabella `room`
--

CREATE TABLE IF NOT EXISTS `room` (
  `RoomName` varchar(45) NOT NULL,
  `Capacity` int(11) NOT NULL,
  `RowNumber` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `room`
--

INSERT INTO `room` (`RoomName`, `Capacity`, `RowNumber`) VALUES
('S.I.1', 80, 5),
('S.I.2', 40, 5),
('S.I.3', 50, 4);

-- --------------------------------------------------------

--
-- Struttura della tabella `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `Username` varchar(45) NOT NULL,
  `Password` varchar(45) NOT NULL,
  `FirstName` varchar(45) NOT NULL,
  `LastName` varchar(45) NOT NULL,
  `MatriculationNumber` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `user`
--

INSERT INTO `user` (`Username`, `Password`, `FirstName`, `LastName`, `MatriculationNumber`) VALUES
('d.lorenzoni2', 'cicciocaputo', 'Dario', 'Lorenzoni', 546619),
('m.pettorali', 'marcom', 'Marco', 'Pettorali', 555444),
('r.nocerino', 'marcomerda', 'Raffaele', 'Nocerino', 530199),
('r.xefraj', 'merdamarco', 'Riccardo', 'Xefraj', 598343);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `booking`
--
ALTER TABLE `booking`
 ADD PRIMARY KEY (`Username`,`StartTime`,`Date`,`PCNumber`,`RoomName`), ADD KEY `PC Number_idx` (`PCNumber`), ADD KEY `Room Name_idx` (`RoomName`);

--
-- Indexes for table `pc`
--
ALTER TABLE `pc`
 ADD PRIMARY KEY (`PCNumber`,`RoomName`), ADD KEY `Room Name_idx` (`RoomName`);

--
-- Indexes for table `room`
--
ALTER TABLE `room`
 ADD PRIMARY KEY (`RoomName`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
 ADD PRIMARY KEY (`Username`);

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `booking`
--
ALTER TABLE `booking`
ADD CONSTRAINT `PC Number` FOREIGN KEY (`PCNumber`) REFERENCES `pc` (`PCNumber`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `Room ` FOREIGN KEY (`RoomName`) REFERENCES `room` (`RoomName`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `Username` FOREIGN KEY (`Username`) REFERENCES `user` (`Username`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limiti per la tabella `pc`
--
ALTER TABLE `pc`
ADD CONSTRAINT `Room Name` FOREIGN KEY (`RoomName`) REFERENCES `room` (`RoomName`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
