package com.example.club.shared.event.data.converter

import com.example.club.shared.event.data.model.HallModel
import com.example.club.shared.event.data.model.SeatPlanModel
import com.example.club.shared.event.data.model.TicketModel
import com.example.club.shared.event.domain.entity.Hall
import com.example.club.shared.event.domain.entity.SeatPlan
import com.example.club.shared.event.domain.entity.Ticket


class HallConverter {
    private fun ticketConverter(ticketModel: TicketModel): Ticket {
        return Ticket(
            id=ticketModel.id,
            seat = ticketModel.seat,
            x=ticketModel.x,
            y=ticketModel.y,
            type = ticketModel.type,
            price = ticketModel.price,
            status = ticketModel.status
        )
    }
    private fun seatPlanConverter(seatPlanModel: SeatPlanModel): SeatPlan {
        return SeatPlan(
            row = seatPlanModel.row,
            column = seatPlanModel.column,
            tickets = seatPlanModel.tickets.map { ticketConverter(it)}
        )
    }
    fun convert(hallModel: HallModel): Hall {
        return Hall(
            id=hallModel.id,
            name = hallModel.name,
            capacity = hallModel.capacity,
            seatingPlan = seatPlanConverter(hallModel.seatingPlan)
        )
    }
}