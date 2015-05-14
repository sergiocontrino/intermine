SELECT cvterm.cvterm_id, cvterm.name as cvterm_name
             FROM cvterm, cv WHERE cv.name = ?
             AND cvterm.cv_id = cv.cv_id;