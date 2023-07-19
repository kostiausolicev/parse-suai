class ApplicantData:
    def __init__(self):
        self.actuality_date = None
        self.position = None

    def set_actuality_date(self, actuality_date: str):
        self.actuality_date = actuality_date
        return self

    def set_position(self, position: int):
        self.position = position
        return self

    def get_actuality_date(self):
        return self.actuality_date

    def get_position(self):
        return self.position

    def to_dict(self) -> dict:
        d = vars(self)
        return d
