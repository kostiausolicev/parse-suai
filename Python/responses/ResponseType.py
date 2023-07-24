from responses.applicants.ApplicantData import ApplicantData
from responses.vector_list.VectorList import VectorList
from responses.vectors_inform.VectorData import VectorData


class ResponseType:
    def __init__(self):
        self.uuid: str = None
        self.tgId: int = None
        self.vectorName: str = None
        self.applicantData: ApplicantData = None
        self.vectorData: VectorData = None
        self.vectorList: list = None
        self.file: list = None

    def has_vectors_list(self):
        return self.vectorList is not None

    def has_applicant_date(self):
        return self.applicantData is not None

    def has_vector_data(self):
        return self.vectorData is not None

    def has_file(self):
        return self.file is not None

    def set_file(self, file):
        self.file = file
        return self

    def set_vector_list(self, vector_list: VectorList):
        self.vectorList = vector_list
        return self

    def set_applicant_data(self, applicant_data):
        self.applicantData = applicant_data
        return self

    def set_vector_data(self, vector_data):
        self.vectorData = vector_data
        return self

    def get_file(self):
        return self.file

    def get_vector_list(self):
        return self.vectorList

    def get_applicant_date(self):
        return self.applicantData

    def get_vector_data(self):
        return self.vectorData

    def get_uuid(self):
        return self.uuid

    def get_vector_name(self):
        return self.vectorName

    def set_vector_name(self, vector_name):
        self.vectorName = vector_name
        return self

    def set_uuid(self, uuid):
        self.uuid = uuid
        return self

    def get_tg_id(self):
        return self.tgId

    def set_tg_id(self, tg_id):
        self.tgId = tg_id
        return self

    def to_dict(self):
        d = vars(self)
        d["applicantData"] = d["applicantData"].to_dict() if self.has_applicant_date() else None
        d["vectorData"] = d["vectorData"].to_dict() if self.has_vector_data() else None
        d["vectorList"] = d["vectorList"].get_vectors_list() if self.has_vectors_list() else None
        return d
