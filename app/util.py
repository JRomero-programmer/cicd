# pylint: disable=no-else-return
def convert_to_number(operand):
    try:
        if "." in operand:
            return float(operand)
        else:
            return int(operand)

    except ValueError:
        raise TypeError("Operator cannot be converted to number")


def InvalidConvertToNumber(operand):
    try:
        if "." in operand:
            return (float(operand))

        return int(operand)

    except ValueError:
        raise TypeError("Operator cannot be converted to number")
        
def validate_permissions(expression, user):
    # Lógica de validación de permisos
    if user == "user1":
        return True
    else:
        return False
