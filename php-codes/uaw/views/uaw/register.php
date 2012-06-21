<h2>Register</h2>
<?php echo validation_errors(); ?>

<?php echo form_open('login/register') ?>
<label for="email">Email</label> 
<input type="input" name="email" value="<?php echo set_value('email'); ?>" /><br />
<label for="password">Password</label>
<input type="password" name="password" /><br />
<label for="repassword">Retype Password</label>
<input type="password" name="repassword" /><br />		
<label for="fname">First Name</label> 
<input type="input" name="fname" value="<?php echo set_value('fname'); ?>" /><br />
<label for="lname">Last Name</label> 
<input type="input" name="lname" value="<?php echo set_value('lname'); ?>" /><br />
<label for="artist">Artist?</label> 
<select name = "artist">
<?php foreach ($types as $type_item): ?>
  <option value="<?php echo $type_item['id']; ?>" <?php echo set_select('artist', $type_item['id']); ?> ><?php echo $type_item['description']; ?></option>   
<?php endforeach ?>
</select>
<br />
<label for="activist">Activist</label> 
<select name="activist">
<option value="Yes" <?php echo set_select('activist', 'Yes'); ?> >Yes</option>
<option value="No" <?php echo set_select('activist', 'No', TRUE); ?> >No</option>
<option value="Maybe" <?php echo set_select('activist', 'Maybe'); ?> >Maybe</option>
</select><br />
<label for="causeworking">Social cause(s) working on</label> 
<select name="causeworking[]" size=4 multiple>
<?php foreach ($causes as $cause_item): ?>
  <option value="<?php echo $cause_item['id']; ?>" <?php echo set_select('causeworking[]', $cause_item['id']); ?> ><?php echo $cause_item['status']; ?></option>   
<?php endforeach ?>
</select><br />
<label for="causeinterest">Social cause(s) interested to know</label> 
<select name="causeinterest[]" size=4 multiple>
<?php foreach ($causes as $cause_item): ?>
  <option value="<?php echo $cause_item['id']; ?>" <?php echo set_select('causeinterest[]', $cause_item['id']); ?> ><?php echo $cause_item['status']; ?></option>   
<?php endforeach ?>
</select><br />
<div class="textfield">
        <?php echo form_label($captcha, 'captcha'); ?>
        <?php echo form_error('captcha'); ?>
        <?php echo form_input('captcha'); ?>
</div>
	<input type="submit" name="submit" value="Register" />
</form>

