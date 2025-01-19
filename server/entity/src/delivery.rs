//! `SeaORM` Entity. Generated by sea-orm-codegen 0.12.10

use sea_orm::entity::prelude::*;

#[derive(Debug, Clone, PartialEq, Eq, EnumIter, DeriveActiveEnum)]
#[sea_orm(rs_type = "String", db_type = "String(StringLen::N(1))")]
pub enum TargetSendFileProtocolAction {
    #[sea_orm(string_value = "B")]
    SoapBill,
    #[sea_orm(string_value = "S")]
    SoapSummary,
    #[sea_orm(string_value = "P")]
    SoapPack,
    #[sea_orm(string_value = "D")]
    RestSendDocument,
}

#[derive(Debug, Clone, PartialEq, Eq, EnumIter, DeriveActiveEnum)]
#[sea_orm(rs_type = "String", db_type = "String(StringLen::N(1))")]
pub enum TargetVerifyTicketProtocolAction {
    #[sea_orm(string_value = "S")]
    SOAP,
    #[sea_orm(string_value = "R")]
    REST,
}

#[derive(Clone, Debug, PartialEq, DeriveEntityModel, Eq)]
#[sea_orm(table_name = "delivery")]
pub struct Model {
    #[sea_orm(primary_key)]
    pub id: i32,
    pub response_ticket: Option<String>,
    pub response_cdr_description: Option<String>,
    pub response_cdr_response_code: Option<String>,
    // pub response_cdr_notes: Option<Vec<String>>,
    pub response_error_code: Option<String>,
    pub response_error_message: Option<String>,

    pub target_send_file_protocol: TargetSendFileProtocolAction,
    pub target_send_file_url: String,

    pub target_verify_ticket_protocol: Option<TargetVerifyTicketProtocolAction>,
    pub target_verify_ticket_url: Option<String>,

    pub document_id: i32,
}

#[derive(Copy, Clone, Debug, EnumIter, DeriveRelation)]
pub enum Relation {
    #[sea_orm(
        belongs_to = "super::document::Entity",
        from = "Column::DocumentId",
        to = "super::document::Column::Id",
        on_update = "NoAction",
        on_delete = "Cascade"
    )]
    Document,
}

impl Related<super::document::Entity> for Entity {
    fn to() -> RelationDef {
        Relation::Document.def()
    }
}

impl ActiveModelBehavior for ActiveModel {}
